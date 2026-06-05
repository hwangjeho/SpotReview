package com.application.spotreview.admin;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.application.spotreview.R;
import com.application.spotreview.model.Review;
import com.application.spotreview.model.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import java.util.ArrayList;
import java.util.List;

public class AdminAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_REVIEW = 0;
    private static final int TYPE_USER = 1;

    private List<Object> items = new ArrayList<>();
    private OnAdminActionListener listener;

    public interface OnAdminActionListener {
        void onReviewAction(Review review);
        void onUserAction(User user);
    }

    public AdminAdapter(OnAdminActionListener listener) {
        this.listener = listener;
    }

    public void setItems(List<?> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Review) return TYPE_REVIEW;
        return TYPE_USER;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_REVIEW) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_review, parent, false);
            return new ReviewViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_user, parent, false);
            return new UserViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ReviewViewHolder) {
            Review review = (Review) items.get(position);
            ReviewViewHolder h = (ReviewViewHolder) holder;
            h.tvUser.setText(review.getUserId());
            h.tvContent.setText(review.getContent());

            if (review.getIsDeleted() == 1) {
                h.card.setCardBackgroundColor(Color.parseColor("#FFEAF0"));
                h.tvStatus.setText("삭제됨");
                h.tvStatus.setTextColor(Color.RED);
                h.btnAction.setText("복원하기");
                h.btnAction.setTextColor(Color.BLUE);
            } else {
                h.card.setCardBackgroundColor(Color.WHITE);
                h.tvStatus.setText("정상");
                h.tvStatus.setTextColor(Color.GRAY);
                h.btnAction.setText("삭제하기");
                h.btnAction.setTextColor(Color.RED);
            }
            h.btnAction.setOnClickListener(v -> listener.onReviewAction(review));

        } else if (holder instanceof UserViewHolder) {
            User user = (User) items.get(position);
            UserViewHolder h = (UserViewHolder) holder;
            h.tvName.setText(user.getName());
            h.tvId.setText("ID: " + user.getUserId());

            if (user.getIsBlocked() == 1) {
                h.btnBlock.setText("차단해제");
                h.btnBlock.setTextColor(Color.BLUE);
            } else {
                h.btnBlock.setText("차단하기");
                h.btnBlock.setTextColor(Color.RED);
            }
            h.btnBlock.setOnClickListener(v -> listener.onUserAction(user));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView tvUser, tvContent, tvStatus;
        MaterialButton btnAction;
        MaterialCardView card;
        ReviewViewHolder(View v) {
            super(v);
            tvUser = v.findViewById(R.id.tv_admin_rev_user);
            tvContent = v.findViewById(R.id.tv_admin_rev_content);
            tvStatus = v.findViewById(R.id.tv_admin_rev_status);
            btnAction = v.findViewById(R.id.btn_admin_rev_action);
            card = v.findViewById(R.id.card_admin_review);
        }
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvId;
        MaterialButton btnBlock;
        UserViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tv_admin_user_name);
            tvId = v.findViewById(R.id.tv_admin_user_id);
            btnBlock = v.findViewById(R.id.btn_admin_user_block);
        }
    }
}
