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

    // 특정 장소의 모든 리뷰 가져오기
    public List<Review> getReviewsBySpot(int spotId) {
        List<Review> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        Cursor cursor = db.rawQuery("SELECT * FROM Review WHERE spot_id = ? ORDER BY created_at DESC", 
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

    // 리뷰 저장
    public boolean insertReview(Review review) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("spot_id", review.getSpotId());
        values.put("user_id", review.getUserId());
        values.put("content", review.getContent());
        // created_at은 DB에서 DEFAULT로 들어가므로 명시하지 않아도 되지만, 
        // 앱에서 정한 시간이 있다면 넣어줄 수 있습니다.
        if (review.getCreatedAt() != null) {
            values.put("created_at", review.getCreatedAt());
        }

        long result = db.insert("Review", null, values);
        db.close();
        return result != -1;
    }
}
