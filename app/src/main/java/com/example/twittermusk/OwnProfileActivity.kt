package com.example.twittermusk

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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlin.math.roundToInt


class OwnProfileActivity : AppCompatActivity() {

    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference

    @RequiresApi(Build.VERSION_CODES.P)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_own_profile)

        val ownEmail = intent.getStringExtra("own_email").toString()
        val picChange: Button = findViewById(R.id.change_pic)
        val picImage: ImageView = findViewById(R.id.profile_pic)
        val username: TextView = findViewById(R.id.Username)

        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference

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

    private fun loadPic(mail: String, picImage: ImageView) {
        try {
            val maxDownloadSize = 10L * 1024 * 1024
            storageReference.child("$mail/profilePic.jpg").getBytes(maxDownloadSize).addOnCompleteListener {
                    val bitmap = BitmapFactory.decodeByteArray(it.result, 0, it.result.size)
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
}