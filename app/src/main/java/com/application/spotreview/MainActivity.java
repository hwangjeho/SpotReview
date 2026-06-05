package com.application.spotreview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.application.spotreview.auth.LoginActivity;
import com.application.spotreview.auth.RegisterActivity;
import com.application.spotreview.map.MapActivity;
import com.application.spotreview.utils.PreferenceManager;
import com.google.android.material.button.MaterialButton;
import com.naver.maps.map.NaverMapSdk;

public class MainActivity extends AppCompatActivity {

    private LinearLayout layoutLoggedOut, layoutLoggedIn;
    private MaterialButton btnLogin, btnRegister, btnExploreMap, btnLogout;
    private TextView tvWelcome, tvRole;

    private PreferenceManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        NaverMapSdk.getInstance(this).setClient(
//                new NaverMapSdk.NaverCloudPlatformClient("r6j06fr3wi")
//        );

        setContentView(R.layout.activity_main);

        // 1. 세션 매니저 초기화
        prefManager = new PreferenceManager(this);

        // 2. 뷰 바인딩
        layoutLoggedOut = findViewById(R.id.layout_logged_out);
        layoutLoggedIn = findViewById(R.id.layout_logged_in);

        btnLogin = findViewById(R.id.btn_main_login);
        btnRegister = findViewById(R.id.btn_main_register);
        btnExploreMap = findViewById(R.id.btn_explore_map);
        btnLogout = findViewById(R.id.btn_main_logout);

        tvWelcome = findViewById(R.id.tv_welcome_message);
        tvRole = findViewById(R.id.tv_user_role);

        // 3. 버튼 이벤트 리스너 설정
        // [로그인 전 상태] 버튼들
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // [로그인 후 상태] 버튼들 MainActivity.java의 onCreate 내부 혹은 버튼 세팅하는 곳에 추가
        btnExploreMap.setOnClickListener(v -> {
            // 버튼 누르면 지도 화면(MapActivity)으로 전환
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            prefManager.logout(); // 세션 삭제
            Toast.makeText(this, "로그아웃되었습니다.", Toast.LENGTH_SHORT).show();
            checkLoginStatus(); // 화면 갱신
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        // 다른 액티비티에서 로그인 성공 후 메인으로 돌아왔을 때 상태를 즉시 반영하기 위함
        checkLoginStatus();
    }

    // 로그인 유무에 따라 UI 레이아웃 토글 처리
    private void checkLoginStatus() {
        if (prefManager.isLoggedIn()) {
            // 로그인 된 상태
            layoutLoggedOut.setVisibility(View.GONE);
            layoutLoggedIn.setVisibility(View.VISIBLE);

            // 데이터 반영 (세션 매니저에서 가져옴)
            String loginId = prefManager.getLoginId();
            tvWelcome.setText(loginId + "님, 환영합니다!");

            if (prefManager.isAdmin()) {
                tvRole.setText("최고 관리자");
                tvRole.setBackgroundColor(android.graphics.Color.parseColor("#FFEAF0"));
                tvRole.setTextColor(android.graphics.Color.parseColor("#D63031"));
            } else {
                tvRole.setText("일반 회원");
                tvRole.setBackgroundColor(android.graphics.Color.parseColor("#E1F5FE"));
                tvRole.setTextColor(android.graphics.Color.parseColor("#0984E3"));
            }
        } else {
            // 로그아웃 된 상태
            layoutLoggedOut.setVisibility(View.VISIBLE);
            layoutLoggedIn.setVisibility(View.GONE);
        }
    }
}