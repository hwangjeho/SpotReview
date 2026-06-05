package com.application.spotreview.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "SpotReview.sqlite";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        // CMD에서 활성화했던 외래키 제약조건 설정 적용
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. User 테이블 생성
        db.execSQL("CREATE TABLE User (" +
                "user_id TEXT PRIMARY KEY, " +
                "password TEXT NOT NULL, " +
                "name TEXT NOT NULL, " +
                "is_blocked INTEGER DEFAULT 0, " +
                "is_admin INTEGER DEFAULT 0);");

        // 2. Spot 테이블 생성
        db.execSQL("CREATE TABLE Spot (" +
                "spot_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "spot_name TEXT NOT NULL, " +
                "latitude REAL NOT NULL, " +
                "longitude REAL NOT NULL, " +
                "address TEXT);");

        // 3. Review 테이블 생성 (유저/스팟 삭제 시 연쇄 삭제 연동)
        db.execSQL("CREATE TABLE Review (" +
                "review_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "spot_id INTEGER, " +
                "user_id TEXT, " +
                "content TEXT NOT NULL, " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "is_deleted INTEGER DEFAULT 0, " +
                "FOREIGN KEY(spot_id) REFERENCES Spot(spot_id) ON DELETE CASCADE, " +
                "FOREIGN KEY(user_id) REFERENCES User(user_id) ON DELETE CASCADE);");

        // 관리자 계정 생성 (테스트용)
        db.execSQL("INSERT INTO User (user_id, password, name, is_admin) VALUES ('admin', 'admin123', '최고관리자', 1);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Review");
        db.execSQL("DROP TABLE IF EXISTS Spot");
        db.execSQL("DROP TABLE IF EXISTS User");
        onCreate(db);
    }
}
