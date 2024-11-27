package com.app.us_twogether.model;

public enum SpaceRole {
    US("us"),
    OTHER_ACTIVITY_READ("other_activity_read"),
    OTHER_ACTIVITY_READ_WRITE("other_activity_read_write"),
    OTHER_FINANCIAL_READ("other_financial_read"),
    OTHER_FINANCIAL_READ_WRITE("other_financial_read_write");

    private String role;

    SpaceRole(String role){
        this.role = role;
    }

    public String getRole() {
        return this.role;
    }
}
