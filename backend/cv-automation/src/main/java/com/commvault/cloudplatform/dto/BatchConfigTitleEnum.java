package com.commvault.cloudplatform.dto;

public enum BatchConfigTitleEnum {

    CLIENT_NAME("clientName", "客户端名"),
    SUBCLIENT_NAME("subclientName", "子客户端名"),
    PATH("path", "路径"),
    STORAGE_POLICY("storagePolicy", "存储策略名"),
    SCHEDULE_POLICY("schedulePolicy", "计划策略名");

    private String prop;

    private String value;

    private BatchConfigTitleEnum(String prop, String value) {
        this.prop = prop;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getProp() {
        return prop;
    }

    public static BatchConfigTitleEnum fromValue(String value) {
        for(BatchConfigTitleEnum type:BatchConfigTitleEnum.values()) {
            if(type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }

        return null;
    }
}
