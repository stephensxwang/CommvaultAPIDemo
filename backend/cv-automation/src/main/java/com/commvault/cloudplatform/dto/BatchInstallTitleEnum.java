package com.commvault.cloudplatform.dto;

public enum BatchInstallTitleEnum {

    COMMCELL_NAME("commCellName", "CommCell"),
    CLIENT_NAME("clientName", "客户端名"),
    USERNAME("userName", "用户名"),
    PASSWORD("password", "密码"),
    OS_TYPE("osType", "系统类型"),
    COMMENT("comment", "备注");

    private String prop;

    private String value;

    private BatchInstallTitleEnum(String prop, String value) {
        this.prop = prop;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getProp() {
        return prop;
    }

    public static BatchInstallTitleEnum fromValue(String value) {
        for(BatchInstallTitleEnum type: BatchInstallTitleEnum.values()) {
            if(type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }

        return null;
    }
}
