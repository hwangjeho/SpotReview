package com.application.spotreview.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.application.spotreview.model.User;
public class UserDao {
    private DatabaseHelper dbHelper;

    public UserDao(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    // [회원가입] 성공 시 true, 아이디 중복 등으로 실패 시 false 반환
    public boolean registerUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", user.getUserId());
        values.put("password", user.getPassword());
        values.put("name", user.getName());
        values.put("is_admin", user.getIsAdmin());

        long result = db.insert("User", null, values);
        db.close();
        return result != -1; // -1이면 에러 발생(중복 키 등)
    }

    // [로그인 검증] 일치하는 회원 데이터를 찾으면 객체 반환, 없으면 null 반환
    public User loginCheck(String userId, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        User foundUser = null;

        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE user_id = ? AND password = ?", new String[]{userId, password});

        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            int isAdmin = cursor.getInt(cursor.getColumnIndexOrThrow("is_admin"));
            foundUser = new User(userId, password, name, isAdmin);
        }
        cursor.close();
        db.close();
        return foundUser;
    }
}
