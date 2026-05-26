package com.application.spotreview.model;

public class User {
    private String userId;
    private String password;
    private String name;
    private int isAdmin; // 일반 유저: 0, 관리자: 1

    // 생성자
    public User(String userId, String password, String name, int isAdmin) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.isAdmin = isAdmin;
    }

    // Getter / Setter
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getIsAdmin() { return isAdmin; }
    public void setIsAdmin(int isAdmin) { this.isAdmin = isAdmin; }
}
