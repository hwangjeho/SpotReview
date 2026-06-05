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

public class SpotRankAdapter extends RecyclerView.Adapter<SpotRankAdapter.RankViewHolder> {

    private List<Review> items = new ArrayList<>();

    public void setItems(List<Review> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spot_rank, parent, false);
        return new RankViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RankViewHolder holder, int position) {
        Review r = items.get(position);
        
        // 순위 표시 (1, 2, 3위는 이모지)
        String rankText;
        if (position == 0) rankText = "🥇";
        else if (position == 1) rankText = "🥈";
        else if (position == 2) rankText = "🥉";
        else rankText = String.valueOf(position + 1);
        
        holder.tvRank.setText(rankText);
        holder.tvName.setText(r.getSpotName());
        holder.tvCount.setText("💬 " + r.getContent() + "개"); // content에 저장된 count 사용
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class RankViewHolder extends RecyclerView.ViewHolder {
        TextView tvRank, tvName, tvCount;
        RankViewHolder(View v) {
            super(v);
            tvRank = v.findViewById(R.id.tv_rank_number);
            tvName = v.findViewById(R.id.tv_rank_spot_name);
            tvCount = v.findViewById(R.id.tv_rank_review_count);
        }
    }
}
