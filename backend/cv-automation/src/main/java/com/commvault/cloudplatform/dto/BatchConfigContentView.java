package com.commvault.cloudplatform.dto;

import java.io.Serializable;

public class BatchConfigContentView implements Serializable {

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getSubclientName() {
        return subclientName;
    }

    public void setSubclientName(String subclientName) {
        this.subclientName = subclientName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getStoragePolicy() {
        return storagePolicy;
    }

    public void setStoragePolicy(String storagePolicy) {
        this.storagePolicy = storagePolicy;
    }

    public String getSchedulePolicy() {
        return schedulePolicy;
    }

    public void setSchedulePolicy(String schedulePolicy) {
        this.schedulePolicy = schedulePolicy;
    }

    private String clientName;
    private String subclientName;
    private String path;
    private String storagePolicy;
    private String schedulePolicy;


}
