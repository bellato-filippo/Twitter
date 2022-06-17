package com.example.twittermusk

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddPostActivity : AppCompatActivity() {

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)
    }

    override fun onStart() {
        super.onStart()

        val createButton: Button = findViewById(R.id.CreateButton)

        createButton.setOnClickListener {
            val mail = intent.getStringExtra("user").toString()
            val text = findViewById<EditText>(R.id.MultiLineCreate).getText().toString()
            val uri = null

            Log.d("POST", "$mail $text $uri")
            // Create a new user with a first, middle, and last name
            val post = hashMapOf(
                "user" to mail,
                "text" to text,
                "picture" to uri
            )

            // Add a new document with a generated ID
            db.collection("posts")
                .add(post)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}