package com.security.demo.security;

public enum UserPermission {
    //This is where you define your permissions
    //you can write them how you like
    STUDENT_READ("student:read"),
    STUDENT_WRITE("student:write"),
    COURSE_READ("course:read"),
    COURSE_WRITE("course:write");

    //this is whats inside the () in the enums
    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
