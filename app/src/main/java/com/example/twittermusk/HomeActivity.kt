package com.example.twittermusk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.twittermusk.adapter.PostAdapter

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val ownEmail = intent.getStringExtra("passed_mail").toString()


//        val myDataset = PostSource().loadPost(ownEmail)
//        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
//        recyclerView.adapter = PostAdapter(this, myDataset)
//
//        recyclerView.setHasFixedSize(true)
    }

    override fun onStart() {
        super.onStart()

        val ownProfile: Button = findViewById(R.id.ProfileButton)
        val newPost: Button = findViewById(R.id.AddButton)
        val ownEmail = intent.getStringExtra("passed_mail").toString()

        newPost.setOnClickListener {
            val intent = Intent(this, AddPostActivity::class.java)
            intent.putExtra("user", ownEmail)
            startActivity(intent)
        }


        ownProfile.setOnClickListener {
            val intent = Intent(this, OwnProfileActivity::class.java)
            intent.putExtra("own_email", ownEmail)
            startActivity(intent)
        }
    }
}