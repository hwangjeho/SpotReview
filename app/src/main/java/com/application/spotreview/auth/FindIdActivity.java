package com.application.spotreview.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.application.spotreview.R;
import com.application.spotreview.database.UserDao;

public class FindIdActivity extends AppCompatActivity {

    private EditText etName;
    private Button btnFindId;
    private TextView tvResult;
    private View cardResult;
    private ImageButton btnBack;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);

        userDao = new UserDao(this);

        etName = findViewById(R.id.et_find_id_name);
        btnFindId = findViewById(R.id.btn_find_id);
        tvResult = findViewById(R.id.tv_find_id_result);
        cardResult = findViewById(R.id.card_result);
        btnBack = findViewById(R.id.btn_find_id_back);

        btnBack.setOnClickListener(v -> finish());

        btnFindId.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            String foundId = userDao.findUserId(name);

            if (foundId != null) {
                cardResult.setVisibility(View.VISIBLE);
                tvResult.setText("찾으시는 아이디는 [" + foundId + "] 입니다.");
            } else {
                cardResult.setVisibility(View.GONE);
                Toast.makeText(this, "일치하는 회원 정보가 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
