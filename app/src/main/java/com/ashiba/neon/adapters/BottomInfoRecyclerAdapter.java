package com.ashiba.neon.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ashiba.neon.R;
import com.ashiba.neon.dataModel.MainPost;
import com.ashiba.neon.views.PostDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BottomInfoRecyclerAdapter extends RecyclerView.Adapter<BottomInfoRecyclerAdapter.ViewHolder>{

    private List<MainPost> bottom_post_list;
    private Context context;

    public BottomInfoRecyclerAdapter(List<MainPost> bottom_post_list, Context context) {
        this.bottom_post_list = bottom_post_list;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View mView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.data_frame_card_layout, viewGroup, false);

        return new ViewHolder(mView);


    }

    @Override
    public void onBindViewHolder(@NonNull BottomInfoRecyclerAdapter.ViewHolder viewHolder, int position) {

        MainPost list = bottom_post_list.get(position);

        viewHolder.mTextView.setText(Html.fromHtml(list.getTitle()));
        viewHolder.mContentView.setText(Html.fromHtml(list.getContent()));
        Picasso.with(context).load(list.getUrl()).into(viewHolder.mImageView);

    }

    @Override
    public int getItemCount() {
        return bottom_post_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView mTextView;
        public TextView mContentView;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.card_frame_image_view);
            mTextView = itemView.findViewById(R.id.card_frame_text_view);
            mContentView = itemView.findViewById(R.id.card_frame_content_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainPost post = bottom_post_list.get(getAdapterPosition());
                    Intent postDetailsIntent = new Intent(context, PostDetailsActivity.class);
                    postDetailsIntent.putExtra("allData",post);
                    context.startActivity(postDetailsIntent);
                }
            });
        }

    }

}
