package com.application.spotreview.map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.application.spotreview.R;
import com.application.spotreview.model.Review;
import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> reviewList = new ArrayList<>();

    public void setReviews(List<Review> reviews) {
        this.reviewList = reviews;
        notifyDataSetChanged();
    }

    public void addReview(Review review) {
        this.reviewList.add(0, review);
        notifyItemInserted(0);
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.tvUser.setText(review.getUserId());
        holder.tvContent.setText(review.getContent());
        holder.tvDate.setText(review.getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView tvUser, tvContent, tvDate;
        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUser = itemView.findViewById(R.id.tv_review_user);
            tvContent = itemView.findViewById(R.id.tv_review_content);
            tvDate = itemView.findViewById(R.id.tv_review_date);
        }
    }
}
