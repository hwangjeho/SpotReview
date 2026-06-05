package com.application.spotreview.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.application.spotreview.model.Review;
import java.util.ArrayList;
import java.util.List;

public class ReviewDao {
    private DatabaseHelper dbHelper;

    public ReviewDao(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    // 특정 장소의 모든 리뷰 가져오기 (정상 상태만)
    public List<Review> getReviewsBySpot(int spotId) {
        List<Review> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Review WHERE spot_id = ? AND is_deleted = 0 ORDER BY created_at DESC", 
                new String[]{String.valueOf(spotId)});
        while (cursor.moveToNext()) {
            int reviewId = cursor.getInt(cursor.getColumnIndexOrThrow("review_id"));
            String userId = cursor.getString(cursor.getColumnIndexOrThrow("user_id"));
            String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
            String createdAt = cursor.getString(cursor.getColumnIndexOrThrow("created_at"));
            list.add(new Review(reviewId, spotId, userId, content, createdAt));
        }
        cursor.close();
        db.close();
        return list;
    }

    public boolean insertReview(Review review) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("spot_id", review.getSpotId());
        values.put("user_id", review.getUserId());
        values.put("content", review.getContent());
        if (review.getCreatedAt() != null) {
            values.put("created_at", review.getCreatedAt());
        }
        long result = db.insert("Review", null, values);
        db.close();
        return result != -1;
    }

    // --- Admin Features ---
    public List<Review> getAllReviewsForAdmin() {
        List<Review> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Review ORDER BY created_at DESC", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("review_id"));
            int spotId = cursor.getInt(cursor.getColumnIndexOrThrow("spot_id"));
            String userId = cursor.getString(cursor.getColumnIndexOrThrow("user_id"));
            String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("created_at"));
            int isDeleted = cursor.getInt(cursor.getColumnIndexOrThrow("is_deleted"));
            Review r = new Review(id, spotId, userId, content, date);
            r.setIsDeleted(isDeleted);
            list.add(r);
        }
        cursor.close();
        db.close();
        return list;
    }

    public boolean updateReviewDeleteStatus(int reviewId, int isDeleted) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_deleted", isDeleted);
        int rows = db.update("Review", values, "review_id = ?", new String[]{String.valueOf(reviewId)});
        db.close();
        return rows > 0;
    }

    // --- Ranking Feature ---
    public List<Review> getSpotReviewCounts() {
        List<Review> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT s.spot_name, COUNT(r.review_id) as review_count " +
                       "FROM Spot s " +
                       "LEFT JOIN Review r ON s.spot_id = r.spot_id AND r.is_deleted = 0 " +
                       "GROUP BY s.spot_id " +
                       "ORDER BY review_count DESC";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            String spotName = cursor.getString(0);
            int count = cursor.getInt(1);
            Review r = new Review(0, 0, "", String.valueOf(count), ""); // count를 content에 임시 저장
            r.setSpotName(spotName);
            list.add(r);
        }
        cursor.close();
        db.close();
        return list;
    }
}
