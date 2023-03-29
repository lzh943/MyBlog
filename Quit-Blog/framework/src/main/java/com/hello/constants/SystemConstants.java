package com.hello.constants;

public class SystemConstants
{
    /**
     * 超级管理员的默认id
     */
    public static final Long ADMIN_ID = 1L;
    /**
     * 表示用户为后台用户
     */
    public static final String ADMIN="1";
    /**
     *  文章是草稿
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;
    /**
     *  文章是正常发布状态
     */
    public static final int ARTICLE_STATUS_NORMAL = 0;
    /**
     * 分类标签处于正常状态
     */
    public static final String CATEGORY_STATUS_NORMAL="0";
    /**
     * 菜单处于正常状态
     */
    public static final String MENU_STATUS_NORMAL="0";
    /**
     * 友链审核通过
     */
    public static final String LINK_STATUS_NORMAL="0";
    /**
     * 评论为根评论
     */
    public static final int COMMENT_ROOTID=-1;
    /**
     * 评论为文章评论
     */
    public static final String COMMENT_COMMENTTYPE="0";
    /**
     * 评论为友链评论
     */
    public static final String COMMENT_LINKTYPE="1";
    /**
     * 文章的浏览次数加1
     */
    public static final Integer ARTICLE_VIEWCOUNT=1;
    /**
     *表示类型为菜单
     */
    public static final String MENU_TYPEC="C";
    /**
     *表示类型为按钮
     */
    public static final String MENU_TYPEF="F";
    /**
     * 表示集合中的元素个数为0
     */
    public static final Integer SIZE=0;
    /**
     * 角色的状态为正常
     */
    public static final String ROLE_STATUS="0";
}
