package com.commvault.cloudplatform.dto;

import java.io.Serializable;

public class BatchConfigTitleView  implements Serializable {

    private String label;
    private String prop;
    private String type;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
