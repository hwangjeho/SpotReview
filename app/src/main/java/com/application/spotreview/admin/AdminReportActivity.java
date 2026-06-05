package com.application.spotreview.admin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.application.spotreview.R;
import com.application.spotreview.database.UserDao;
import com.application.spotreview.database.ReviewDao;
import com.application.spotreview.model.Review;
import com.application.spotreview.model.User;
import java.util.List;

public class AdminReportActivity extends AppCompatActivity implements AdminAdapter.OnAdminActionListener {

    private RecyclerView rvAdmin;
    private Button btnManageReviews, btnManageUsers;
    private ImageButton btnBack;
    private AdminAdapter adapter;
    private UserDao userDao;
    private ReviewDao reviewDao;
    private boolean isReviewMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_report);

        userDao = new UserDao(this);
        reviewDao = new ReviewDao(this);

        rvAdmin = findViewById(R.id.rv_admin_list);
        btnManageReviews = findViewById(R.id.btn_manage_reviews);
        btnManageUsers = findViewById(R.id.btn_manage_users);
        btnBack = findViewById(R.id.btn_admin_back);

        adapter = new AdminAdapter(this);
        rvAdmin.setLayoutManager(new LinearLayoutManager(this));
        rvAdmin.setAdapter(adapter);

        loadReviewManagement();

        btnManageReviews.setOnClickListener(v -> loadReviewManagement());
        btnManageUsers.setOnClickListener(v -> loadUserManagement());
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadReviewManagement() {
        isReviewMode = true;
        List<Review> reviews = reviewDao.getAllReviewsForAdmin();
        adapter.setItems(reviews);
    }

    private void loadUserManagement() {
        isReviewMode = false;
        List<User> users = userDao.getAllUsers();
        adapter.setItems(users);
    }

    @Override
    public void onReviewAction(Review review) {
        int newStatus = (review.getIsDeleted() == 1) ? 0 : 1;
        if (reviewDao.updateReviewDeleteStatus(review.getReviewId(), newStatus)) {
            loadReviewManagement();
        }
    }

    @Override
    public void onUserAction(User user) {
        int newStatus = (user.getIsBlocked() == 1) ? 0 : 1;
        if (userDao.updateUserBlockStatus(user.getUserId(), newStatus)) {
            loadUserManagement();
        }
    }
}
