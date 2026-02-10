// UpdateUserRequest.java
package com.meetingscheduler.dto;

public class UpdateUserRequest {
    private String username;
    private String password; // ✅ new field

    // --- getters & setters ---
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
