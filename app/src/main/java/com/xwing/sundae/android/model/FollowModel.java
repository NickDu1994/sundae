package com.xwing.sundae.android.model;

public class FollowModel {

    private String eventDesc;
    private String eventTime;
    private String itemName;
    private String itemContent;
    private String itemImage;

    public FollowModel(String eventDesc, String eventTime, String itemName, String itemContent, String itemImage) {
        this.eventDesc = eventDesc;
        this.eventTime = eventTime;
        this.itemName = itemName;
        this.itemContent = itemContent;
        this.itemImage = itemImage;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
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

    public void setItemContent(String itemDesc) {
        this.itemContent = itemDesc;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }
}
