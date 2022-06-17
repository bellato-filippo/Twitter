package com.example.twittermusk.data

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.example.twittermusk.models.Post
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class PostSource {

    val db = Firebase.firestore


    fun loadPost(user: String): List<Post> {



        return listOf<Post>()

//        val posts = mutableListOf<Post>()
//        db.collection("posts")
//            .whereEqualTo("user", user)
//            .get()
//            .addOnSuccessListener { documents ->
//                for (document in documents) {
//                    val email: String = document.data.getValue("user").toString()
//                    val uri: Uri? = if (document.data.getValue("picture") == null){
//                        null
//                    } else {
//                        Uri.parse(document.data.getValue("picture").toString())
//                    }
//                    //val picture: String = document.data.getValue("picture").toString()
//                    val text: String = document.data.getValue("text").toString()
//
//                    val post = Post(email, uri, text)
//                    Log.d(TAG, "${posts.size}")
//                    posts.add(post)
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.w(TAG, "Error getting documents: ", exception)
//            }
//
//        return posts
    }

}

interface ResultListener {
    fun onResult(data: List<Post>)
    fun onError(error: Throwable)
}