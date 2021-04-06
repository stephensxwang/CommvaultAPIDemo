package com.commvault.cloudplatform.dto;

import java.io.Serializable;
import java.util.List;

public class BatchInstallView implements Serializable {

    private List<BatchInstallTitleView> titleList;
    private List<BatchInstallContentView> contentList;

    public List<BatchInstallTitleView> getTitleList() {
        return titleList;
    }

    public void setTitleList(List<BatchInstallTitleView> titleList) {
        this.titleList = titleList;
    }

    public List<BatchInstallContentView> getContentList() {
        return contentList;
    }

    public void setContentList(List<BatchInstallContentView> contentList) {
        this.contentList = contentList;
    }

}
