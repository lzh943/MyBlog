package com.hello.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentVo {
    private Long id;
    //文章id
    private Long articleId;
    //根评论id
    private Long rootId;
    //评论内容
    private String content;
    //所回复的目标评论的userid
    private Long toCommentUserId;
    //所回复的目标评论的昵称
    private String toCommentUserName;
    //回复目标评论id
    private Long toCommentId;
    //发表此评论的userid
    private Long createBy;

    private Date createTime;
    //用户的昵称
    private String username;

    private List<CommentVo> children;

}
