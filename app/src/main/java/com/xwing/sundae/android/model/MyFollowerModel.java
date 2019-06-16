package com.xwing.sundae.android.model;

import java.util.Date;

/**
 * Author: Maggie
 * Date: 2019/6/6
 * Time: 17:38
 */
public class MyFollowerModel {
    /**
     * 关注者id
     */
    private Long follow_user_id;
    /**
     * 关注者昵称
     */
    private String follow_username;
    /**
     * 关注者头像
     */
    private String follow_avatarUrl;
    /**
     * 关注时间
     */
    private String followTime;
    /**
     * 发布条数
     */
    private String sentCounts;
    /**
     * 获赞条数
     */
    private String getPraisedCounts;

    /**
     * 构造器
     *
     * @param avatarUrl
     * @param followTime
     * @param sentCounts
     * @param getPraisedCounts
     */
    public MyFollowerModel(Long follow_user_id,
                           String follow_username,
                           String avatarUrl,
                           String followTime,
                           String sentCounts,
                           String getPraisedCounts) {
        this.follow_user_id = follow_user_id;
        this.follow_username = follow_username;
        this.follow_avatarUrl = avatarUrl;
        this.followTime = followTime;
        this.sentCounts = sentCounts;
        this.getPraisedCounts = getPraisedCounts;
    }

    public String getFollowTime() {
        return followTime;
    }

    public void setFollowTime(String followTime) {
        this.followTime = followTime;
    }

    public String getSentCounts() {
        return sentCounts;
    }

    public void setSentCounts(String sentCounts) {
        this.sentCounts = sentCounts;
    }

    public String getGetPraisedCounts() {
        return getPraisedCounts;
    }

    public void setGetPraisedCounts(String getPraisedCounts) {
        this.getPraisedCounts = getPraisedCounts;
    }

    public Long getFollow_user_id() {
        return follow_user_id;
    }

    public void setFollow_user_id(Long follow_user_id) {
        this.follow_user_id = follow_user_id;
    }

    public String getFollow_avatarUrl() {
        return follow_avatarUrl;
    }

    public void setFollow_avatarUrl(String follow_avatarUrl) {
        this.follow_avatarUrl = follow_avatarUrl;
    }

    public String getFollow_username() {
        return follow_username;
    }

    public void setFollow_username(String follow_username) {
        this.follow_username = follow_username;
    }
}
