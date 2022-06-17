package com.example.twittermusk

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.twittermusk.adapter.PostAdapter
import com.example.twittermusk.models.Post
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
class HomeActivity : AppCompatActivity() {

    val db = Firebase.firestore
    var myDataset = mutableListOf<Post>()
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val ownEmail = intent.getStringExtra("passed_mail").toString()
        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        Log.d("DEBUG", "trya succesfully called")
        trya(ownEmail)
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

    fun trya(user: String) {
        val posts = mutableListOf<Post>()
        Log.d("DEBUG", "Inside the function")
        db.collection("posts")
            .whereEqualTo("user", user)
            .get()
            .addOnSuccessListener { documents ->
                val p = mutableListOf<Post>()
                for (document in documents) {
                    val email: String = document.data.getValue("user").toString()
                    val uri: String = if (document.data.getValue("picture") == null){
                        ""
                    } else {
                        document.data.getValue("picture").toString()
                    }
                    //val picture: String = document.data.getValue("picture").toString()
                    val text: String = document.data.getValue("text").toString()

                    val post = Post(email, uri, text)
                    Log.d(TAG, "${p.size}")
                    p.add(post)
                }
                myDataset = p
                recyclerView.adapter = PostAdapter(this, myDataset)
                recyclerView.setHasFixedSize(true)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }
}