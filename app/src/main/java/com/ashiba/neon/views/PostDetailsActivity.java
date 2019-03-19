package com.ashiba.neon.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashiba.neon.R;
import com.ashiba.neon.dataModel.MainPost;
import com.squareup.picasso.Picasso;

public class PostDetailsActivity extends AppCompatActivity {

    private ImageView  mImageViewDetails;
    private TextView  mTextViewDetails, mContentViewDetails;
    MainPost post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        mImageViewDetails = findViewById(R.id.image_post_details);
        mTextViewDetails = findViewById(R.id.title_post_details);
        mContentViewDetails = findViewById(R.id.content_post_details);


        post = (MainPost) getIntent().getSerializableExtra("allData");

        mTextViewDetails.setText(Html.fromHtml(post.getTitle()));

        mContentViewDetails.setText(Html.fromHtml(post.getContent()));






        Picasso.with(PostDetailsActivity.this).load(post.getUrl()).into(mImageViewDetails);
    }

}
