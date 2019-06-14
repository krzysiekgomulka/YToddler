package com.abc.ytoddler.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.abc.ytoddler.R;
import com.abc.ytoddler.models.YoutubeCommentModel;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.YoutubeCommentHolder> {

    private ArrayList<YoutubeCommentModel> dataSet;
    private Context mContext = null;

    public CommentAdapter(Context mContext, ArrayList<YoutubeCommentModel> data) {
        this.dataSet = data;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CommentAdapter.YoutubeCommentHolder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.youtube_comment_layout, parent, false);
        return new YoutubeCommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YoutubeCommentHolder holder, int position) {
        TextView textViewName = holder.name;
        TextView feedback = holder.feedback;
        ImageView imageView = holder.icon;
        YoutubeCommentModel object = dataSet.get(position);
        textViewName.setText(object.getTitle());
        feedback.setText(object.getComment());
        try {
            if (object.getThumbnail() != null) {
                if (object.getThumbnail().startsWith("http")) {
                    Picasso.get()
                            .load(object.getThumbnail())
                            .into(imageView);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    static class YoutubeCommentHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView feedback;
        ImageView icon;

        YoutubeCommentHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.textViewName);
            this.icon = itemView.findViewById(R.id.profile_image);
            this.feedback = itemView.findViewById(R.id.feedback);

        }

    }

}
