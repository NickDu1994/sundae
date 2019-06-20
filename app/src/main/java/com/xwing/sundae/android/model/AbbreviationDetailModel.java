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
}
