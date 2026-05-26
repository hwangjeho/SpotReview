package com.application.spotreview.utils;

import android.content.Context;
import android.content.SharedPreferences;
public class PreferenceManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_IS_ADMIN = "isAdmin";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public PreferenceManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    // 로그인 처리 시 세션 저장
    public void login(String userId, String userName, int isAdmin) {
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_USER_NAME, userName);
        editor.putInt(KEY_IS_ADMIN, isAdmin);
        editor.apply();
    }

    // 로그인 여부 확인
    public boolean isLoggedIn() {
        return pref.getString(KEY_USER_ID, null) != null;
    }

    // 로그인된 유저 ID 가져오기
    public String getLoginId() {
        return pref.getString(KEY_USER_ID, "");
    }

    // 관리자 여부 확인
    public boolean isAdmin() {
        return pref.getInt(KEY_IS_ADMIN, 0) == 1;
    }

    // [로그아웃] 세션 데이터 일괄 삭제
    public void logout() {
        editor.clear();
        editor.apply();
    }
}
