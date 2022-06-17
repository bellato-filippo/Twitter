package com.example.twittermusk

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlin.math.roundToInt


class OwnProfileActivity : AppCompatActivity() {

    private var uriProfilePic: Uri? = null
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference

    @RequiresApi(Build.VERSION_CODES.P)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_own_profile)

        val ownEmail = intent.getStringExtra("own_email").toString()
        val picChange: Button = findViewById(R.id.change_pic)
        val picImage: ImageView = findViewById(R.id.profile_pic)

        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference

        val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) {

            uriProfilePic = it

            val source = ImageDecoder.createSource(this.contentResolver, it)
            val bitmap = ImageDecoder.decodeBitmap(source)
            val imageHeight = bitmap.height
            val imageWidth = bitmap.width
            val proportion: Double
            val metrics = resources.displayMetrics
            val dpi = metrics.densityDpi
            val pixels = 238 * (dpi / 160)

            if (imageHeight < imageWidth) {
                proportion = imageWidth.toDouble() / pixels.toDouble()
                val newHeight = imageHeight / proportion.roundToInt()
                Picasso.get().load(uriProfilePic).resize(pixels, newHeight).into(picImage)
            } else {
                proportion = imageHeight.toDouble() / pixels.toDouble()
                val newWidth = (imageWidth / proportion).roundToInt()
                Picasso.get().load(uriProfilePic).resize(newWidth, pixels).into(picImage)
            }

            uploadImageToDB(ownEmail)
        }

        picChange.setOnClickListener {
            getImage.launch("image/*")
        }


    }


    private fun uploadImageToDB(s: String) {
        if (uriProfilePic == null) return

        val pPicRef = storageReference.child("$s/profilePic.jpg")

        pPicRef.putFile(uriProfilePic!!)
            .addOnSuccessListener {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Image Uploaded",
                    Snackbar.LENGTH_LONG
                ).show()
            }
    }
}