package com.commvault.cloudplatform.dto;

import java.io.Serializable;
import java.util.List;

public class Db2DataRestoreView implements Serializable {
    private String srcSubclientName;
    private String srcBackupsetName;
    private String srcInstanceName;
    private String srcClientName;
    private String destClientName;
    private String destInstanceName;
    private String targetDbName;
    private String targetPath;
    private String backupDate;

    public String getSrcSubclientName() {
        return srcSubclientName;
    }

    public void setSrcSubclientName(String srcSubclientName) {
        this.srcSubclientName = srcSubclientName;
    }

    public String getSrcBackupsetName() {
        return srcBackupsetName;
    }

    public void setSrcBackupsetName(String srcBackupsetName) {
        this.srcBackupsetName = srcBackupsetName;
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
