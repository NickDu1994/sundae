package com.xwing.sundae.android.model;

import java.util.Date;

/**
 * Author
 * Date: 2019/6/13
 * Time: 10:34
 */
public class MyPublishModel {

    /**
     * 词条创建时间
     */
    private Date create_time;
    /**
     * 词条类型
     */
    private String abb_type;
    /**
     * 词条标题
     */
    private String item_name;
    /**
     * 词条内容
     */
    private String item_content;
    /**
     * 词条获赞数
     */
    private String abb_likedCount;
    /**
     * 词条全称
     */
    private String abb_fullName;
    /**
     * 词条图片
     */
    private String item_image;

    /**
     * 构造器
     * @param create_time
     * @param abb_type
     * @param item_name
     * @param item_content
     * @param abb_likedCount
     * @param abb_fullName
     * @param item_image
     */
    public MyPublishModel(Date create_time,
                          String abb_type,
                          String item_name,
                          String item_content,
                          String abb_likedCount,
                          String abb_fullName,
                          String item_image) {
        this.create_time = create_time;
        this.item_name = item_name;
        this.abb_type = abb_type;
        this.item_content = item_content;
        this.abb_likedCount = abb_likedCount;
        this.abb_fullName = abb_fullName;
        this.item_image = item_image;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getAbb_type() {
        return abb_type;
    }

    public void setAbb_type(String abb_type) {
        this.abb_type = abb_type;
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

    public String getAbb_likedCount() {
        return abb_likedCount;
    }

    public void setAbb_likedCount(String abb_likedCount) {
        this.abb_likedCount = abb_likedCount;
    }

    public String getAbb_fullName() {
        return abb_fullName;
    }

    public void setAbb_fullName(String abb_fullName) {
        this.abb_fullName = abb_fullName;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }
}
