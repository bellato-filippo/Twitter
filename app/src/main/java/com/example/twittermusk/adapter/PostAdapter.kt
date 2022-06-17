package com.example.twittermusk.adapter

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.twittermusk.R
import com.example.twittermusk.models.Post
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldPath.documentId
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PostAdapter(
    private val context: Context,
    private val dataSet: List<Post>,
    private val self: String,
    private val other: String,
    private val keys: List<String>
    ): RecyclerView.Adapter<PostAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        val textViewUser: TextView = view.findViewById(R.id.postUser)
        val textViewText: TextView = view.findViewById(R.id.postText)
        val buttonFollow: Button = view.findViewById<Button>(R.id.followButton)
        val buttonLike: Button = view.findViewById(R.id.likeButton)
        val like: TextView = view.findViewById(R.id.like)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_post, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataSet[position]
        holder.textViewUser.text = item.user
        holder.textViewText.text = item.text
        holder.buttonFollow.setOnClickListener {
            if (self.equals(other)) {
                Toast.makeText(context, "You can't follow yourself", Toast.LENGTH_SHORT).show()
            }

            Firebase.firestore.collection("follow")
                .whereEqualTo("me", self)
                .get()
                .addOnSuccessListener { documents ->
                    val me = mutableListOf<String>()
                    for (document in documents) {
                        me.add(document.data.getValue("following").toString())
                    }

                    if (me.contains(other)) {
                        Toast.makeText(context, "You already follow this user", Toast.LENGTH_SHORT).show()
                    } else {
                        val f = hashMapOf(
                            "me" to self,
                            "following" to other
                        )

                        Firebase.firestore.collection("follow")
                            .add(f)
                            .addOnSuccessListener { documentReference ->
                                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                            }
                            .addOnFailureListener { e ->
                                Log.w(ContentValues.TAG, "Error adding document", e)
                            }
                    }
                }

        }

        holder.buttonLike.setOnClickListener {
            Firebase.firestore.collection("like")
                .whereEqualTo("user", self)
                .get()
                .addOnSuccessListener { documents ->
                    val me = mutableListOf<String>()
                    for (document in documents) {
                        me.add(document.data.getValue("post").toString())
                    }

                    if (me.contains(keys[position])) {
                        Toast.makeText(context, "You already liked this post", Toast.LENGTH_SHORT).show()
                    } else {
                        val l = hashMapOf(
                            "user" to self,
                            "post" to keys[position]
                        )
                        Firebase.firestore.collection("like")
                            .add(l)
                            .addOnSuccessListener { documentReference ->
                                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                                Firebase.firestore.collection("like")
                                    .whereEqualTo("post", keys[position])
                                    .get()
                                    .addOnSuccessListener { documents ->
                                        holder.like.text = documents.size().toString()
                                    }
                            }
                            .addOnFailureListener { e ->
                                Log.w(ContentValues.TAG, "Error adding document", e)
                            }
                    }
                }
        }

        Firebase.firestore.collection("like")
            .whereEqualTo("post", keys[position])
            .get()
            .addOnSuccessListener { documents ->
                holder.like.text = documents.size().toString()
            }
    }
}