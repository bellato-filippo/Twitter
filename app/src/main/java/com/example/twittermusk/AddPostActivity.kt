package com.example.twittermusk

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
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
            // Create a new user with a first, middle, and last name
            val user = hashMapOf(
                "first" to "Alan",
                "middle" to "Mathison",
                "last" to "Turing",
                "born" to 1912
            )

            // Add a new document with a generated ID
            db.collection("users")
                .add(user)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }
    }
}