package com.example.twittermusk

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.twittermusk.adapter.PostAdapter
import com.example.twittermusk.models.Post
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlin.math.roundToInt


class OwnProfileActivity : AppCompatActivity() {

    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private lateinit var ownProfilePost: RecyclerView
    private var ownEmail: String = ""
    private var myDataset = mutableListOf<Post>()


    @RequiresApi(Build.VERSION_CODES.P)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_own_profile)

        ownEmail = intent.getStringExtra("own_email").toString()
        val picChange: Button = findViewById(R.id.change_pic)
        val picImage: ImageView = findViewById(R.id.profile_pic)
        val username: TextView = findViewById(R.id.Username)
        ownProfilePost = findViewById(R.id.profile_posts)

        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference
        Log.d("testprecrash0","$ownEmail")
        username.text = "Username:\n$ownEmail"
        loadPic(ownEmail, picImage)

        val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) {

            val source = ImageDecoder.createSource(this.contentResolver, it)
            val bitmap = ImageDecoder.decodeBitmap(source)

            updatePic(bitmap, picImage)

            uploadImageToDB(ownEmail,it)
        }

        picChange.setOnClickListener {
            getImage.launch("image/*")
        }


    }

    override fun onStart() {
        super.onStart()
        getData()
    }

    private fun loadPic(mail: String, picImage: ImageView) {
        try {
            val maxDownloadSize = 10L * 1024 * 1024
            storageReference.child("$mail/profilePic.jpg").getBytes(maxDownloadSize).addOnSuccessListener {
                    val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                    updatePic(bitmap, picImage)
                }
        } catch (e: Exception) {
            Log.d("test","c'è un'eccezione mannaggina $e")
            return
        }


    }

    private fun updatePic(bitmap: Bitmap, picImage: ImageView) {
        val imageHeight = bitmap.height
        val imageWidth = bitmap.width
        val proportion: Double
        val metrics = resources.displayMetrics
        val dpi = metrics.densityDpi
        val pixels = 238 * (dpi / 160)

        if (imageHeight < imageWidth) {
            proportion = imageWidth.toDouble() / pixels.toDouble()
            val newHeight = imageHeight / proportion.roundToInt()
            picImage.setImageBitmap(Bitmap.createScaledBitmap(bitmap, pixels, newHeight, false))
        } else {
            proportion = imageHeight.toDouble() / pixels.toDouble()
            val newWidth = (imageWidth / proportion).roundToInt()
            picImage.setImageBitmap(Bitmap.createScaledBitmap(bitmap, newWidth, pixels, false))
        }
    }


    private fun uploadImageToDB(s: String, uriProfilePic: Uri) {

        val pPicRef = storageReference.child("$s/profilePic.jpg")

        pPicRef.putFile(uriProfilePic)
            .addOnSuccessListener {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Image Uploaded",
                    Snackbar.LENGTH_LONG
                ).show()
            }
    }

    private fun getData(){
        Firebase.firestore.collection("posts")
            .whereEqualTo("user", ownEmail)
            .get()
            .addOnSuccessListener { documents ->
                val p = mutableListOf<Post>()
                val keys = mutableListOf<String>()
                for (document in documents) {
                    keys.add(document.id)
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
                ownProfilePost.adapter = PostAdapter(this, myDataset, ownEmail, ownEmail, keys)
                ownProfilePost.setHasFixedSize(true)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }
}