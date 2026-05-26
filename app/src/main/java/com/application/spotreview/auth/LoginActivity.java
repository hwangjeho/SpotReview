package com.application.spotreview.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.application.spotreview.R;
import com.application.spotreview.database.UserDao;
import com.application.spotreview.model.User;
import com.application.spotreview.utils.PreferenceManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
public class LoginActivity extends AppCompatActivity{
    private TextInputLayout tilId, tilPw;
    private TextInputEditText etId, etPw;
    private MaterialButton btnSubmit;
    private TextView tvGoRegister;

    private UserDao userDao;
    private PreferenceManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 1. 핵심 객체 초기화
        userDao = new UserDao(this);
        prefManager = new PreferenceManager(this);

        // 2. 컴포넌트 연결
        tilId = findViewById(R.id.til_login_id);
        tilPw = findViewById(R.id.til_login_pw);
        etId = findViewById(R.id.et_login_id);
        etPw = findViewById(R.id.et_login_pw);
        btnSubmit = findViewById(R.id.btn_login_submit);
        tvGoRegister = findViewById(R.id.tv_go_to_register);

        // 3. 로그인 버튼 클릭 리스너
        btnSubmit.setOnClickListener(v -> {
            if (validateInputs()) {
                performLogin();
            }
        });

        // 4. 회원가입 하러가기 텍스트 클릭 리스너
        tvGoRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish(); // 로그인 창을 닫으면서 회원가입 창으로 완전히 전환
        });
    }

    // 입력값 유효성 검사
    private boolean validateInputs() {
        boolean isValid = true;
        String id = etId.getText().toString().trim();
        String pw = etPw.getText().toString().trim();

        if (id.isEmpty()) {
            tilId.setError("아이디를 입력해 주세요.");
            isValid = false;
        } else {
            tilId.setError(null);
        }

        if (pw.isEmpty()) {
            tilPw.setError("비밀번호를 입력해 주세요.");
            isValid = false;
        } else {
            tilPw.setError(null);
        }

        return isValid;
    }

    // 실제 로그인 처리 (DB 대조 및 세션 저장)
    private void performLogin() {
        String id = etId.getText().toString().trim();
        String pw = etPw.getText().toString().trim();

        // SQLite DB 내에 일치하는 사용자가 있는지 확인
        User matchedUser = userDao.loginCheck(id, pw);

        if (matchedUser != null) {
            // 일치하는 회원이 있으면 SharedPreference에 세션 저장
            prefManager.login(matchedUser.getUserId(), matchedUser.getName(), matchedUser.getIsAdmin());

            Toast.makeText(this, matchedUser.getName() + "님, 환영합니다!", Toast.LENGTH_SHORT).show();
            finish(); // 로그인 완료 후 메인 화면으로 복귀 (MainActivity의 onResume이 돌면서 UI 변경됨)
        } else {
            // 회원 정보가 틀렸거나 없는 경우 가이드 제공
            tilId.setError("아이디 또는 비밀번호가 올바르지 않습니다.");
            tilPw.setError("아이디 또는 비밀번호가 올바르지 않습니다.");
        }
    }
}
