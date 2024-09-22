package com.example.comrades

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)
        enableEdgeToEdge()

        Thread() {
            val mp : MediaPlayer = MediaPlayer.create(this, R.raw.sound)
            mp.start()
            val source : ImageDecoder.Source = ImageDecoder.createSource(
                resources, R.raw.splash2
            )
            val drawable : Drawable = ImageDecoder.decodeDrawable(source)
            val imageView : ImageView = findViewById<ImageView>(R.id.splash_image)
            imageView.post {
                imageView.setImageDrawable(drawable)
                (drawable as? AnimatedImageDrawable)?.start()
            }

            mp.setOnCompletionListener() {
                startActivity(Intent(this, LoginActivity::class.java),
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            }
        }.start()
    }
}