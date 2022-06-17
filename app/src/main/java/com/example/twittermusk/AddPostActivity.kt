package com.example.twittermusk

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.math.roundToInt

class AddPostActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference
    private val uuid: String = UUID.randomUUID().toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

    }


    @RequiresApi(Build.VERSION_CODES.P)

    override fun onStart() {
        super.onStart()

        val createButton: Button = findViewById(R.id.CreateButton)
        //val addImage: Button = findViewById(R.id.AddImageButton)
        //val preview: ImageView = findViewById(R.id.postPreview)

        val mail = intent.getStringExtra("own_mail").toString()


//        val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
//
//            val source = ImageDecoder.createSource(this.contentResolver, it)
//            val bitmap = ImageDecoder.decodeBitmap(source)
//
//            uri = "$mail/$uuid.jpg"
//
//            updatePic(bitmap, preview)
//
//
//            Log.d("prima di database", "$uuid $uri")
//            uploadImageToDB(it, uri)
//        }

//        addImage.setOnClickListener {
//            getImage.launch("image/*")
//        }

        createButton.setOnClickListener {

            //uri = "$mail/$uuid.jpg"
            //Log.d("quale è uuuidididid", "$uuid $uri")

            val text = findViewById<EditText>(R.id.MultiLineCreate).getText().toString()
            var uri : String = ""

            Log.d("POST", "$mail $text $uri")
            // Create a new user with a first, middle, and last name
            val post = hashMapOf(
                "user" to mail,
                "text" to text,
                "picture" to ""
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
            intent.putExtra("own_mail", mail)
            startActivity(intent)
        }
    }

//    private fun updatePic(bitmap: Bitmap, picImage: ImageView) {
//        val imageHeight = bitmap.height
//        val imageWidth = bitmap.width
//        val proportion: Double
//        val metrics = resources.displayMetrics
//        val dpi = metrics.densityDpi
//        val pixels = 400 * (dpi / 160)
//
//        if (imageHeight < imageWidth) {
//            proportion = imageWidth.toDouble() / pixels.toDouble()
//            val newHeight = imageHeight / proportion.roundToInt()
//            picImage.setImageBitmap(Bitmap.createScaledBitmap(bitmap, pixels, newHeight, false))
//        } else {
//            proportion = imageHeight.toDouble() / pixels.toDouble()
//            val newWidth = (imageWidth / proportion).roundToInt()
//            picImage.setImageBitmap(Bitmap.createScaledBitmap(bitmap, newWidth, pixels, false))
//        }
//    }

//    private fun uploadImageToDB(uriProfilePic: Uri?, uri: String) {
//
//        Log.d("dentro db", "$uuid $uri")
//        val pPicRef = storageReference.child(uri)
//
//        pPicRef.putFile(uriProfilePic!!)
//            .addOnSuccessListener {
//                Snackbar.make(
//                    findViewById(android.R.id.content),
//                    "Image Uploaded",
//                    Snackbar.LENGTH_LONG
//                ).show()
//            }
//    }

}