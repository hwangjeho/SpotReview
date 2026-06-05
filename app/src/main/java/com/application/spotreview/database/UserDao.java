package com.application.spotreview.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.application.spotreview.model.User;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private DatabaseHelper dbHelper;

    public UserDao(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    public boolean registerUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", user.getUserId());
        values.put("password", user.getPassword());
        values.put("name", user.getName());
        values.put("is_admin", user.getIsAdmin());
        long result = db.insert("User", null, values);
        db.close();
        return result != -1;
    }

    public User loginCheck(String userId, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        User foundUser = null;
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE user_id = ? AND password = ? AND is_blocked = 0", new String[]{userId, password});
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            int isAdmin = cursor.getInt(cursor.getColumnIndexOrThrow("is_admin"));
            foundUser = new User(userId, password, name, isAdmin);
        }
        cursor.close();
        db.close();
        return foundUser;
    }

    public String findUserId(String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String foundId = null;
        Cursor cursor = db.rawQuery("SELECT user_id FROM User WHERE name = ?", new String[]{name});
        if (cursor.moveToFirst()) {
            foundId = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return foundId;
    }

    public boolean verifyUser(String userId, String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE user_id = ? AND name = ?", new String[]{userId, name});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public boolean updatePassword(String userId, String newPassword) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", newPassword);
        int rows = db.update("User", values, "user_id = ?", new String[]{userId});
        db.close();
        return rows > 0;
    }

    // --- Admin Features ---
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE is_admin = 0", null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow("user_id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            int isBlocked = cursor.getInt(cursor.getColumnIndexOrThrow("is_blocked"));
            // Password is not needed for management list
            User user = new User(id, "", name, 0);
            user.setIsBlocked(isBlocked); 
            list.add(user);
        }
        cursor.close();
        db.close();
        return list;
    }

    public boolean updateUserBlockStatus(String userId, int isBlocked) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_blocked", isBlocked);
        int rows = db.update("User", values, "user_id = ?", new String[]{userId});
        db.close();
        return rows > 0;
    }
}
