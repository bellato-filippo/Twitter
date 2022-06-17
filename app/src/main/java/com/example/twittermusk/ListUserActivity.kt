package com.example.twittermusk

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.twittermusk.adapter.PostAdapter
import com.example.twittermusk.models.Post
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ListUserActivity : AppCompatActivity() {

    val db = Firebase.firestore
    var myDataset = mutableListOf<Post>()
    lateinit var recyclerView: RecyclerView
    var ownMail: String = ""
    var otherMail: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_user)

        ownMail = intent.getStringExtra("own_mail").toString()
        otherMail = intent.getStringExtra("other_mail").toString()

        Log.d("DEBUG", ownMail)
        Log.d("DEBUG", otherMail)

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)

        db.collection("posts")
            .whereEqualTo("user", otherMail)
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
                    Log.d(ContentValues.TAG, "${p.size}")
                    p.add(post)
                }
                myDataset = p
                recyclerView.adapter = PostAdapter(this, myDataset, ownMail, otherMail)
                recyclerView.setHasFixedSize(true)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }
}