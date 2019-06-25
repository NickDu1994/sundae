package com.xwing.sundae.android.model;

public class FollowModel {

    private String event_desc;
    private String event_time;
    private String item_name;
    private String item_content;
    private String item_image;
    private String item_username;
    private String item_avatar;
    private String item_date;

    public FollowModel(String event_desc, String event_time, String item_name, String item_content, String item_image, String item_username, String item_avatar, String item_date, Long id) {
        this.event_desc = event_desc;
        this.event_time = event_time;
        this.item_name = item_name;
        this.item_content = item_content;
        this.item_image = item_image;
        this.item_username = item_username;
        this.item_avatar = item_avatar;
        this.item_date = item_date;
        this.id = id;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;

    public String getEvent_desc() {
        return event_desc;
    }

    public void setEvent_desc(String event_desc) {
        this.event_desc = event_desc;
    }

    public String getEvent_time() {
        return event_time;
    }

    public void setEvent_time(String event_time) {
        this.event_time = event_time;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_content() {
        return item_content;
    }

    public void setItem_content(String item_content) {
        this.item_content = item_content;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public String getItem_username() {
        return item_username;
    }

    public void setItem_username(String item_username) {
        this.item_username = item_username;
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

    public FollowModel(String event_desc, String event_time, String item_name, String item_content, String item_image, String item_username, String item_avatar, String item_date) {

        this.event_desc = event_desc;
        this.event_time = event_time;
        this.item_name = item_name;
        this.item_content = item_content;
        this.item_image = item_image;
        this.item_username = item_username;
        this.item_avatar = item_avatar;
        this.item_date = item_date;
    }




}
