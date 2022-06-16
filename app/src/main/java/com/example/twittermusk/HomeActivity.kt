package com.example.twittermusk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    override fun onStart() {
        super.onStart()

        val newPost: Button = findViewById(R.id.AddButton)

        newPost.setOnClickListener {
            val intent = Intent(this, AddPostActivity::class.java)
            startActivity(intent)
        }

        val ownProfile: Button = findViewById(R.id.ProfileButton)

        ownProfile.setOnClickListener {
            val intent = Intent(this, OwnProfileActivity::class.java)
            startActivity(intent)
        }
    }
}