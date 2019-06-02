package com.xwing.sundae.android.model;

public class ComplexListModel {

    private String mainTitle;
    private String mainContent;
    private String[] imagesList;
    private boolean additionalInformation1;
    private String additionalInformation2;
    private String additionalInformation3;

    public ComplexListModel(String mainTitle, String mainContent, String[] imagesList, boolean additionalInformation1, String additionalInformation2, String additionalInformation3) {
        this.mainTitle = mainTitle;
        this.mainContent = mainContent;
        this.imagesList = imagesList;
        this.additionalInformation1 = additionalInformation1;
        this.additionalInformation2 = additionalInformation2;
        this.additionalInformation3 = additionalInformation3;
    }

    public String getMainTitle() {
        return mainTitle;
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    public String getMainContent() {
        return mainContent;
    }

    public void setMainContent(String mainContent) {
        this.mainContent = mainContent;
    }

    public String[] getImagesList() {
        return imagesList;
    }

    public void setImagesList(String[] imagesList) {
        this.imagesList = imagesList;
    }

    public boolean isAdditionalInformation1() {
        return additionalInformation1;
    }

    public void setAdditionalInformation1(boolean additionalInformation1) {
        this.additionalInformation1 = additionalInformation1;
    }

    public String getAdditionalInformation2() {
        return additionalInformation2;
    }

    public void setAdditionalInformation2(String additionalInformation2) {
        this.additionalInformation2 = additionalInformation2;
    }

    public String getAdditionalInformation3() {
        return additionalInformation3;
    }

    public void setAdditionalInformation3(String additionalInformation3) {
        this.additionalInformation3 = additionalInformation3;
    }
}
