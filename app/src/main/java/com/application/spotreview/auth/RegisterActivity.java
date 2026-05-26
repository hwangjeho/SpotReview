package com.application.spotreview.auth;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.application.spotreview.R;
import com.application.spotreview.database.UserDao;
import com.application.spotreview.model.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity{
    private TextInputLayout tilId, tilPw, tilName;
    private TextInputEditText etId, etPw, etName;
    private MaterialButton btnSubmit;
    private TextView tvBackLogin;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 1. 초기화
        userDao = new UserDao(this);

        tilId = findViewById(R.id.til_register_id);
        tilPw = findViewById(R.id.til_register_pw);
        tilName = findViewById(R.id.til_register_name);

        etId = findViewById(R.id.et_register_id);
        etPw = findViewById(R.id.et_register_pw);
        etName = findViewById(R.id.et_register_name);

        btnSubmit = findViewById(R.id.btn_register_submit);
        tvBackLogin = findViewById(R.id.tv_back_to_login);

        // 2. 가입 버튼 클릭
        btnSubmit.setOnClickListener(v -> {
            if (validateInputs()) {
                performRegistration();
            }
        });

        // 3. 로그인 돌아가기 클릭
        tvBackLogin.setOnClickListener(v -> finish());
    }

    // 유효성 검사 (현대적인 에러 표시 방식)
    private boolean validateInputs() {
        boolean isValid = true;

        String id = etId.getText().toString().trim();
        String pw = etPw.getText().toString().trim();
        String name = etName.getText().toString().trim();

        if (id.isEmpty()) {
            tilId.setError("아이디를 입력하세요");
            isValid = false;
        } else {
            tilId.setError(null);
        }

        if (pw.isEmpty()) {
            tilPw.setError("비밀번호를 입력하세요");
            isValid = false;
        } else if (pw.length() < 4) {
            tilPw.setError("비밀번호는 4자 이상이어야 합니다");
            isValid = false;
        } else {
            tilPw.setError(null);
        }

        if (name.isEmpty()) {
            tilName.setError("이름을 입력하세요");
            isValid = false;
        } else {
            tilName.setError(null);
        }

        return isValid;
    }

    // 실제 가입 처리
    private void performRegistration() {
        String id = etId.getText().toString().trim();
        String pw = etPw.getText().toString().trim();
        String name = etName.getText().toString().trim();

        User newUser = new User(id, pw, name, 0); // 0: 일반유저

        boolean isSuccess = userDao.registerUser(newUser);

        if (isSuccess) {
            Toast.makeText(this, "환영합니다, " + name + "님!", Toast.LENGTH_SHORT).show();
            finish(); // 로그인 화면으로 이동
        } else {
            tilId.setError("이미 사용 중인 아이디입니다.");
        }
    }
}
