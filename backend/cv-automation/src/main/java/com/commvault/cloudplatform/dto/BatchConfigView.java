package com.commvault.cloudplatform.dto;

import java.io.Serializable;
import java.util.List;

public class BatchConfigView implements Serializable {

    private List<BatchConfigTitleView> titleList;
    private List<BatchConfigContentView> contentList;

    public List<BatchConfigTitleView> getTitleList() {
        return titleList;
    }

    public void setTitleList(List<BatchConfigTitleView> titleList) {
        this.titleList = titleList;
    }

    public List<BatchConfigContentView> getContentList() {
        return contentList;
    }

    public void setContentList(List<BatchConfigContentView> contentList) {
        this.contentList = contentList;
    }

}
