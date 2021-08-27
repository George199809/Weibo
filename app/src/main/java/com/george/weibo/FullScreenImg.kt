package com.george.weibo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView

class FullScreenImg : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_img)
        val iv_photo = findViewById<PhotoView>(R.id.iv_photo)
        Glide.with(this).load(intent.getStringExtra("url")).into(iv_photo)

    }
}