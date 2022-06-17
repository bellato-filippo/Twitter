package com.example.twittermusk

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
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
    var ownMail: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        ownMail = intent.getStringExtra("own_mail").toString()
        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        getData(ownMail)
    }


    override fun onStart() {
        super.onStart()

        val ownProfile: Button = findViewById(R.id.ProfileButton)
        val newPost: Button = findViewById(R.id.AddButton)
//        val ownEmail = intent.getStringExtra("passed_mail").toString()

        newPost.setOnClickListener {
            val intent = Intent(this, AddPostActivity::class.java)
            intent.putExtra("user", ownMail)
            startActivity(intent)
        }


        ownProfile.setOnClickListener {
            val intent = Intent(this, OwnProfileActivity::class.java)
            intent.putExtra("own_email", ownMail)
            startActivity(intent)
        }

        val search: Button = findViewById(R.id.SearchButton)

        search.setOnClickListener {
            val intent = Intent(this, ListUserActivity::class.java)
            val name = findViewById<EditText>(R.id.EditTextSearch).getText().toString()
            intent.putExtra("other_mail", name)
            intent.putExtra("own_mail", ownMail)
            startActivity(intent)
        }
    }

    fun getData(user: String) {
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
                recyclerView.adapter = PostAdapter(this, myDataset, ownMail, ownMail)
                recyclerView.setHasFixedSize(true)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }
}