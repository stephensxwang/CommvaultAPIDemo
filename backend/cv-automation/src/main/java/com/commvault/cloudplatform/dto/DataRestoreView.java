package com.commvault.cloudplatform.dto;

import java.io.Serializable;
import java.util.List;

public class DataRestoreView implements Serializable {
    private int srcClientId;
    private String srcClientName;
    private String backupTime;
    private int targetClientId;
    private String targetClientName;
    private String destPath;
    private List<String> filePaths;

    public int getSrcClientId() {
        return srcClientId;
    }

    public void setSrcClientId(int srcClientId) {
        this.srcClientId = srcClientId;
    }

    public String getSrcClientName() {
        return srcClientName;
    }

    public void setSrcClientName(String srcClientName) {
        this.srcClientName = srcClientName;
    }

    public String getBackupTime() {
        return backupTime;
    }

    public void setBackupTime(String backupTime) {
        this.backupTime = backupTime;
    }

    public int getTargetClientId() {
        return targetClientId;
    }

    public void setTargetClientId(int targetClientId) {
        this.targetClientId = targetClientId;
    }

    public String getTargetClientName() {
        return targetClientName;
    }

    public void setTargetClientName(String targetClientName) {
        this.targetClientName = targetClientName;
    }

    public String getDestPath() {
        return destPath;
    }

    public void setDestPath(String destPath) {
        this.destPath = destPath;
    }

    public List<String> getFilePaths() {
        return filePaths;
    }

    public void setFilePaths(List<String> filePaths) {
        this.filePaths = filePaths;
    }
}
