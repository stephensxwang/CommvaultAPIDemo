package com.commvault.cloudplatform.dto;

public enum OsTypeEnum {

    WINDOWS("Windows"),
    UNIX("Unix");

    private String value;

    private OsTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static OsTypeEnum fromValue(String value) {
        for(OsTypeEnum type: OsTypeEnum.values()) {
            if(type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }

        return null;
    }
}
