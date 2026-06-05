package com.application.spotreview.map;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.application.spotreview.R;
import com.application.spotreview.database.ReviewDao;
import com.application.spotreview.database.SpotDao;
import com.application.spotreview.model.Review;
import com.application.spotreview.utils.PreferenceManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SpotDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvAddress;
    private RecyclerView rvReviews;
    private EditText etReviewInput;
    private ImageButton btnSendReview, btnBack;
    private ReviewAdapter adapter;
    private PreferenceManager prefManager;
    private SpotDao spotDao;
    private ReviewDao reviewDao;
    private int currentSpotId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_detail);

        prefManager = new PreferenceManager(this);
        spotDao = new SpotDao(this);
        reviewDao = new ReviewDao(this);

        tvTitle = findViewById(R.id.tv_detail_title);
        tvAddress = findViewById(R.id.tv_detail_address);
        rvReviews = findViewById(R.id.rv_reviews);
        etReviewInput = findViewById(R.id.et_review_input);
        btnSendReview = findViewById(R.id.btn_send_review);
        btnBack = findViewById(R.id.btn_detail_back);

        // 뒤로가기 버튼
        btnBack.setOnClickListener(v -> finish());

        // Intent 데이터 수신
        String title = getIntent().getStringExtra("title");
        String address = getIntent().getStringExtra("address");
        double lat = getIntent().getDoubleExtra("lat", 0.0);
        double lng = getIntent().getDoubleExtra("lng", 0.0);

        tvTitle.setText(title);
        tvAddress.setText(address);

        // DB에서 Spot 정보 가져오거나 생성
        currentSpotId = spotDao.getOrCreateSpot(title, address, lat, lng);

        // 리사이클러뷰 설정
        adapter = new ReviewAdapter();
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        rvReviews.setAdapter(adapter);

        // DB에서 기존 리뷰 로드
        loadReviews();

        // 리뷰 전송
        btnSendReview.setOnClickListener(v -> {
            String content = etReviewInput.getText().toString().trim();
            if (content.isEmpty()) {
                Toast.makeText(this, "리뷰를 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            String userId = prefManager.getLoginId();
            if (userId == null || userId.isEmpty()) userId = "익명";

            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            // 리뷰 객체 생성 (DB 저장을 위해 spot_id 포함)
            Review newReview = new Review(0, currentSpotId, userId, content, currentTime);
            
            // DB 저장
            if (reviewDao.insertReview(newReview)) {
                // 어댑터 갱신 (실시간 반영)
                adapter.addReview(newReview);
                etReviewInput.setText("");
                rvReviews.smoothScrollToPosition(0);
                Toast.makeText(this, "리뷰가 등록되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "리뷰 등록 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadReviews() {
        List<Review> reviews = reviewDao.getReviewsBySpot(currentSpotId);
        adapter.setReviews(reviews);
    }
}
