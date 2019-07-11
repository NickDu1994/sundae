package com.xwing.sundae.android.model;

/**
 * Created by Android Studio.
 * User: dell
 * Date: 2019/6/19
 * Time: 20:25
 */
public class AbbreviationDetailModel {
    private boolean like;
    private AbbreviationPlusModel abbreviation;
    private boolean collect;
    private boolean follow;
    private String author;
    private String avatar;
    private String collectNumber;

    public String getCollectNumber() {
        return collectNumber;
    }

    public void setCollectNumber(String collectNumber) {
        this.collectNumber = collectNumber;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public AbbreviationPlusModel getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(AbbreviationPlusModel abbreviation) {
        this.abbreviation = abbreviation;
    }

    public boolean isCollect() {
        return collect;
    }

    public void setCollect(boolean collect) {
        this.collect = collect;
    }

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
