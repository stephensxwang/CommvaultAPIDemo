package com.commvault.cloudplatform.dto;

import java.io.Serializable;

public class MssqlDataRestoreView implements Serializable {
    private String srcDbName;
    private String srcInstanceName;
    private String srcClientName;
    private String destClientName;
    private String destInstanceName;
    private String targetDbName;
    private String targetPath;
    private String backupDate;

    public String getSrcDbName() {
        return srcDbName;
    }

    public void setSrcDbName(String srcDbName) {
        this.srcDbName = srcDbName;
    }

    public String getSrcInstanceName() {
        return srcInstanceName;
    }

    public void setSrcInstanceName(String srcInstanceName) {
        this.srcInstanceName = srcInstanceName;
    }

    public String getSrcClientName() {
        return srcClientName;
    }

    public void setSrcClientName(String srcClientName) {
        this.srcClientName = srcClientName;
    }

    public String getDestClientName() {
        return destClientName;
    }

    public void setDestClientName(String destClientName) {
        this.destClientName = destClientName;
    }

    public String getDestInstanceName() {
        return destInstanceName;
    }

    public void setDestInstanceName(String destInstanceName) {
        this.destInstanceName = destInstanceName;
    }

    public String getTargetDbName() {
        return targetDbName;
    }

    public void setTargetDbName(String targetDbName) {
        this.targetDbName = targetDbName;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public String getBackupDate() {
        return backupDate;
    }

    public void setBackupDate(String backupDate) {
        this.backupDate = backupDate;
    }
}
