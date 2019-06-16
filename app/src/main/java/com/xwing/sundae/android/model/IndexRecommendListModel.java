package com.xwing.sundae.android.model;

import java.util.List;

public class IndexRecommendListModel {
    private String id;
    private String userId;
    private String abbrName;
    private String fullName;
    private String content;
    private String imageId;
    private String dataStatus;
    private String createTime;
    private String createBy;
    private List<IndexBannerImage.Image> imageList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAbbrName() {
        return abbrName;
    }

    public void setAbbrName(String abbrName) {
        this.abbrName = abbrName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public List<IndexBannerImage.Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<IndexBannerImage.Image> imageList) {
        this.imageList = imageList;
    }
}
