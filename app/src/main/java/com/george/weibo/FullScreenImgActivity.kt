package com.george.weibo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView

class FullScreenImgActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_img)
        val iv_photo = findViewById<PhotoView>(R.id.iv_photo)
        Glide.with(this).load(intent.getStringExtra("url")).into(iv_photo)
    }

    companion object{
        fun start(context : Context, url : String){
            val intent = Intent(context, FullScreenImgActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("url", url)
            context.startActivity(intent)
        }
    }
}