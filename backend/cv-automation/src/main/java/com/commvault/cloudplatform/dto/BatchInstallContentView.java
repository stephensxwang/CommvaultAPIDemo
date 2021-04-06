package com.commvault.cloudplatform.dto;

import java.io.Serializable;

public class BatchInstallContentView implements Serializable {
    private String clientName;
    private String userName;
    private String password;
    private String osType;
    private String commCellName;
    private String comment;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getCommCellName() {
        return commCellName;
    }

    public void setCommCellName(String commCellName) {
        this.commCellName = commCellName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
