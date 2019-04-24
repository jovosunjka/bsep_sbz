package com.bsep.SiemCenterRules.model;


import com.bsep.SiemCenterRules.model.enums.RelationalOperator;

public class Condition {
    private String field;
    private Object value;
    private RelationalOperator operator;

    public Condition() {

    }

    public Condition(String field, Object value, RelationalOperator operator) {
        this.field = field;
        this.value = value;
        this.operator = operator;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public RelationalOperator getOperator() {
        return operator;
    }

    public void setOperator(RelationalOperator operator) {
        this.operator = operator;
    }
}
