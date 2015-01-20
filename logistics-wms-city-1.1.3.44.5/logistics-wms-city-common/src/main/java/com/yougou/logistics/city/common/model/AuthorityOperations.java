package com.yougou.logistics.city.common.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class AuthorityOperations {
    private Integer operationId;

    private String operationName;

    private String operationComment;

    private Short operationType;

    @JsonProperty("id")
    public Integer getOperationId() {
        return operationId;
    }

    public void setOperationId(Integer operationId) {
        this.operationId = operationId;
    }

    @JsonProperty("text")
    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    @JsonIgnore
    public String getOperationComment() {
        return operationComment;
    }

    public void setOperationComment(String operationComment) {
        this.operationComment = operationComment;
    }

    public Short getOperationType() {
        return operationType;
    }

    public void setOperationType(Short operationType) {
        this.operationType = operationType;
    }
}