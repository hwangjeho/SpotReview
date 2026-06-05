package com.application.spotreview.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.application.spotreview.R;
import com.application.spotreview.database.UserDao;

public class FindPwActivity extends AppCompatActivity {

    private EditText etId, etName, etNewPw;
    private Button btnVerify, btnReset;
    private LinearLayout layoutVerify, layoutReset;
    private ImageButton btnBack;
    private UserDao userDao;
    private String verifiedUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

        userDao = new UserDao(this);

        etId = findViewById(R.id.et_find_pw_id);
        etName = findViewById(R.id.et_find_pw_name);
        etNewPw = findViewById(R.id.et_new_pw);
        btnVerify = findViewById(R.id.btn_verify_user);
        btnReset = findViewById(R.id.btn_reset_pw);
        layoutVerify = findViewById(R.id.layout_verify_user);
        layoutReset = findViewById(R.id.layout_reset_pw);
        btnBack = findViewById(R.id.btn_find_pw_back);

        btnBack.setOnClickListener(v -> finish());

        // Step 1: 인증하기
        btnVerify.setOnClickListener(v -> {
            String userId = etId.getText().toString().trim();
            String name = etName.getText().toString().trim();

            if (userId.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "아이디와 이름을 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (userDao.verifyUser(userId, name)) {
                verifiedUserId = userId;
                layoutVerify.setVisibility(View.GONE);
                layoutReset.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "일치하는 회원 정보가 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // Step 2: 비밀번호 변경
        btnReset.setOnClickListener(v -> {
            String newPw = etNewPw.getText().toString().trim();

            if (newPw.isEmpty()) {
                Toast.makeText(this, "새 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (userDao.updatePassword(verifiedUserId, newPw)) {
                Toast.makeText(this, "비밀번호가 성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                finish(); // 로그인 화면으로 돌아감
            } else {
                Toast.makeText(this, "비밀번호 변경에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
