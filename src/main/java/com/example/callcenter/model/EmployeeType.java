package com.example.callcenter.model;

public enum EmployeeType {
    OPERATOR (1),
    SUPERVISOR(2),
    DIRECTOR(3);

    private final int typeCode;

    EmployeeType(int typeCode) {
        this.typeCode = typeCode;
    }

    public int getTypeCode() {
        return typeCode;
    }
}
