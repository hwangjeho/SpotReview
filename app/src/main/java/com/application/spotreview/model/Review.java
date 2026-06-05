package com.application.spotreview.model;

public class Review {
    private int reviewId;
    private int spotId;
    private String userId;
    private String content;
    private String createdAt;
    private int isDeleted; // 삭제됨: 1, 정상: 0
    private String spotName; // 랭킹 기능을 위해 추가

    public Review(int reviewId, int spotId, String userId, String content, String createdAt) {
        this.reviewId = reviewId;
        this.spotId = spotId;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
        this.isDeleted = 0;
    }

    // Getter / Setter
    public int getReviewId() { return reviewId; }
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }

    public int getSpotId() { return spotId; }
    public void setSpotId(int spotId) { this.spotId = spotId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public int getIsDeleted() { return isDeleted; }
    public void setIsDeleted(int isDeleted) { this.isDeleted = isDeleted; }

    public String getSpotName() { return spotName; }
    public void setSpotName(String spotName) { this.spotName = spotName; }
}
