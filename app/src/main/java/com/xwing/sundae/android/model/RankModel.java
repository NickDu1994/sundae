package com.xwing.sundae.android.model;

public class RankModel {
    private int itemRank;
    private String itemRankImage;
    private String itemName;
    private String itemContent;
    private String itemImage;
    private String itemAvatar;
    private String authorName;
    private String likeNumber;

    public RankModel(int itemRank, String itemRankImage, String itemName, String itemContent, String itemImage, String itemAvatar, String authorName, String likeNumber) {
        this.itemRank = itemRank;
        this.itemRankImage = itemRankImage;
        this.itemName = itemName;
        this.itemContent = itemContent;
        this.itemImage = itemImage;
        this.itemAvatar = itemAvatar;
        this.authorName = authorName;
        this.likeNumber = likeNumber;
    }

    public int getItemRank() {
        return itemRank;
    }

    public void setItemRank(int itemRank) {
        this.itemRank = itemRank;
    }

    public String getItemRankImage() {
        return itemRankImage;
    }

    public void setItemRankImage(String itemRankImage) {
        this.itemRankImage = itemRankImage;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemContent() {
        return itemContent;
    }

    public void setItemContent(String itemContent) {
        this.itemContent = itemContent;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemAvatar() {
        return itemAvatar;
    }

    public void setItemAvatar(String itemAvatar) {
        this.itemAvatar = itemAvatar;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getLikeNumber() {
        return likeNumber;
    }

    public void setLikeNumber(String likeNumber) {
        this.likeNumber = likeNumber;
    }
}
