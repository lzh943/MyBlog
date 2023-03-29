package com.hello.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hello.constants.SystemConstants;
import com.hello.domain.ResponseResult;
import com.hello.domain.entity.Comment;
import com.hello.domain.vo.CommentVo;
import com.hello.domain.vo.PageVo;
import com.hello.exception.SystemException;
import com.hello.enums.AppHttpCodeEnum;
import com.hello.mapper.CommentMapper;
import com.hello.mapper.UserMapper;
import com.hello.service.CommentService;
import com.hello.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Comment> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.eq(Comment ::getRootId, SystemConstants.COMMENT_ROOTID);
        queryWrapper.eq(Comment::getArticleId, articleId );
        queryWrapper.eq(Comment::getType, SystemConstants.COMMENT_COMMENTTYPE);
        Page<Comment> commentPage=new Page(pageNum,pageSize);
        page(commentPage, queryWrapper);
        List<CommentVo> commentVoList= toCommentVoList(commentPage.getRecords());
        //查询对应的子评论
        for (CommentVo commentVo : commentVoList) {
            List<CommentVo> children=getChildren(commentVo.getId());
            commentVo.setChildren(children);
        }
        return ResponseResult.okResult(new PageVo(commentVoList, commentPage.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        if(!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult linkCommentList(Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Comment> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId, SystemConstants.COMMENT_ROOTID);
        queryWrapper.eq(Comment::getType, SystemConstants.COMMENT_LINKTYPE);
        Page<Comment> commentPage=new Page<>(pageNum,pageSize);
        page(commentPage, queryWrapper);
        List<CommentVo> commentVoList=toCommentVoList(commentPage.getRecords());
        for (CommentVo commentVo : commentVoList) {
            List<CommentVo> children=getChildren(commentVo.getId());
            commentVo.setChildren(children);
        }
        return ResponseResult.okResult(new PageVo(commentVoList, commentPage.getTotal()));
    }

    private List<CommentVo> getChildren(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId, id);
        queryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> comments=list(queryWrapper);
        List<CommentVo> children=toCommentVoList(comments);
        return children;
    }

    private List<CommentVo> toCommentVoList(List<Comment> commentList){
        List<CommentVo> commentVos=BeanCopyUtils.copyBeanList(commentList, CommentVo.class);
        for(CommentVo commentVo : commentVos){
            String nickName=userMapper.selectById(commentVo.getCreateBy()).getNickName();
            commentVo.setUsername(nickName);
            if(commentVo.getToCommentUserId()!=-1){
                String toNickName=userMapper.selectById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(toNickName);
            }
        }
        return commentVos;
    }
}
