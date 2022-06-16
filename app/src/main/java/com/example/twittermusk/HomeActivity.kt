package com.example.twittermusk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.twittermusk.adapter.PostAdapter
import com.example.twittermusk.data.PostSource

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val myDataset = PostSource().loadPost()
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = PostAdapter(this, myDataset)

        recyclerView.setHasFixedSize(true)
    }

    override fun onStart() {
        super.onStart()

        val newPost: Button = findViewById(R.id.AddButton)

        newPost.setOnClickListener {
            val intent = Intent(this, AddPostActivity::class.java)
            startActivity(intent)
        }
    }
}