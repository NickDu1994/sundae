package com.xwing.sundae.android.model;

/**
 * Author: Maggie
 * Date: 2019/6/8
 * Time: 22:40
 */
public class MyCollectModel {

    /**
     * 收藏id
     */
    private Long item_id;
    /**
     * 收藏的封面图片
     */
    private String item_image;
    /**
     * 收藏的title
     */
    private String item_name;
    /**
     * 收藏的内容
     */
    private String item_content;
    /**
     * 收藏的作者
     */
    private String collect_author;
    /**
     * 收藏的时间
     */
    private String collect_time;

    public MyCollectModel(){}

    public MyCollectModel(Long item_id, String item_image, String item_name, String item_content, String collect_author, String collect_time) {
        this.item_id = item_id;
        this.item_image = item_image;
        this.item_name = item_name;
        this.item_content = item_content;
        this.collect_author = collect_author;
        this.collect_time = collect_time;
    }

    public Long getItem_id() {
        return item_id;
    }

    public void setItem_id(Long item_id) {
        this.item_id = item_id;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
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

    public String getCollect_author() {
        return collect_author;
    }

    public void setCollect_author(String collect_author) {
        this.collect_author = collect_author;
    }

    public String getCollect_time() {
        return collect_time;
    }

    public void setCollect_time(String collect_time) {
        this.collect_time = collect_time;
    }
}
