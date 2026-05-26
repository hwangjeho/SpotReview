package com.application.spotreview.model;

public class Review {
    private int reviewId;
    private int spotId;
    private String userId;
    private String content;
    private String createdAt;

    public Review(int reviewId, int spotId, String userId, String content, String createdAt) {
        this.reviewId = reviewId;
        this.spotId = spotId;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
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
}
