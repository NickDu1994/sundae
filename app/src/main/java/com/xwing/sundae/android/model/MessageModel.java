package com.xwing.sundae.android.model;

public class MessageModel {
    private String content;
    private String createTime;
    private String dataStatus;
    private Long id;
    private String readFlag;
    private String userId;
    private int type;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReadFlag() {
        return readFlag;
    }

    public void setReadFlag(String readFlag) {
        this.readFlag = readFlag;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public MessageModel(String content, String createTime, String dataStatus, Long id, String readFlag, String userId, int type) {
        this.content = content;
        this.createTime = createTime;
        this.dataStatus = dataStatus;
        this.id = id;
        this.readFlag = readFlag;
        this.userId = userId;
        this.type = type;
    }
}
