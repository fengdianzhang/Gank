package com.kunkka.gank;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

public class GalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ImageView iv = (ImageView) findViewById(R.id.photo);
        String url = getIntent().getStringExtra("url");
        Picasso.with(this)
                .load(url)
                .placeholder(R.drawable.placeholder)
                .into(iv);
    }
}
