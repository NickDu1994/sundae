package com.xwing.sundae.android.model;

public class AbbreviationPlusModel extends AbbreviationBaseModel {
    private String lastUpdateTime;
    private String type;
    private String likedCount;
    private String visitedCount;
    private String image;

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLikedCount() {
        return likedCount;
    }

    public void setLikedCount(String likedCount) {
        this.likedCount = likedCount;
    }

    public String getVisitedCount() {
        return visitedCount;
    }

    public void setVisitedCount(String visitedCount) {
        this.visitedCount = visitedCount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
