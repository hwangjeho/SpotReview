package com.application.spotreview.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SpotDao {
    private DatabaseHelper dbHelper;

    public SpotDao(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    // 장소(Spot)가 있으면 가져오고, 없으면 새로 저장하고 그 ID를 반환
    public int getOrCreateSpot(String name, String address, double lat, double lng) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int spotId = -1;

        // 먼저 이름과 주소로 존재하는지 확인
        Cursor cursor = db.rawQuery("SELECT spot_id FROM Spot WHERE spot_name = ? AND address = ?", new String[]{name, address});
        if (cursor.moveToFirst()) {
            spotId = cursor.getInt(0);
        } else {
            // 없으면 새로 삽입
            ContentValues values = new ContentValues();
            values.put("spot_name", name);
            values.put("address", address);
            values.put("latitude", lat);
            values.put("longitude", lng);
            long result = db.insert("Spot", null, values);
            spotId = (int) result;
        }
        cursor.close();
        db.close();
        return spotId;
    }
}
