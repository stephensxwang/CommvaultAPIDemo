package com.commvault.cloudplatform.controller.groovy

import com.commvault.cloudplatform.dto.BatchConfigContentView

class BatchConfigRule {

    Map getConfigValue(BatchConfigContentView batchConfigContentView) {
        def result = [:]

        result.put("clientName", batchConfigContentView.getClientName() ? "not null" : "null")

        return result;
    }
}