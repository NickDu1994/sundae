package com.xwing.sundae.android.model;

public class CommentModel {
    private Long id;
    private String userId;
    private String username;
    private String postId;
    private float item_rate;
    private String item_content;
    private String item_avatar;
    private String item_date;

    public CommentModel(Long id, String userId, String username, String postId, float item_rate, String item_content, String item_avatar, String item_date) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.postId = postId;
        this.item_rate = item_rate;
        this.item_content = item_content;
        this.item_avatar = item_avatar;
        this.item_date = item_date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public float getItem_rate() {
        return item_rate;
    }

    public void setItem_rate(float item_rate) {
        this.item_rate = item_rate;
    }

    public String getItem_content() {
        return item_content;
    }

    public void setItem_content(String item_content) {
        this.item_content = item_content;
    }

    public String getItem_avatar() {
        return item_avatar;
    }

    public void setItem_avatar(String item_avatar) {
        this.item_avatar = item_avatar;
    }

    public String getItem_date() {
        return item_date;
    }

    public void setItem_date(String item_date) {
        this.item_date = item_date;
    }
}
