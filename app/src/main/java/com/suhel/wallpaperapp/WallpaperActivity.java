package com.suhel.wallpaperapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.IOException;

public class WallpaperActivity extends AppCompatActivity {

    private ImageView wallpaperIV;
    private Button setWallpaperBtn;
    private String imgUrl;
    WallpaperManager wallpaperManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);

        wallpaperIV=findViewById(R.id.idIVWallpaper);
        setWallpaperBtn=findViewById(R.id.idBtnSetWallpaper);
        imgUrl=getIntent().getStringExtra("imgUrl");
        Glide.with(this).load(imgUrl).into(wallpaperIV);
        wallpaperManager=WallpaperManager.getInstance(getApplicationContext());
        setWallpaperBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(WallpaperActivity.this).asBitmap().load(imgUrl).listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        Toast.makeText(WallpaperActivity.this, "Fail to load image...", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        try {
                            wallpaperManager.setBitmap(resource);
                        }catch (IOException e){
                            e.printStackTrace();
                            Toast.makeText(WallpaperActivity.this, "Fail to set wallpaper...", Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                }).submit();
                Toast.makeText(WallpaperActivity.this, "Wallpaper set to Homescreen", Toast.LENGTH_LONG).show();

            }
        });
    }
}