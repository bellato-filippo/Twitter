package com.example.twittermusk

import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class OwnProfileActivity : AppCompatActivity() {

    private var uriProfilePic: Uri? = null
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private val user = FirebaseAuth.getInstance().currentUser.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_own_profile)

        val profileBtn: ImageButton = findViewById(R.id.profileButton)

        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference

        val getImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {

                uriProfilePic = it

                profileBtn.setImageURI(uriProfilePic)

                uploadImageToDB()
            }
        )

        profileBtn.setOnClickListener{
            getImage.launch("image/*")
        }



    }

    private fun uploadImageToDB() {
        if (uriProfilePic==null) return

        val pPicRef = storageReference.child("$user/profilePic.jpg")

        pPicRef.putFile(uriProfilePic!!)
            .addOnSuccessListener {
                Snackbar.make(findViewById(android.R.id.content), "Image Uploaded",Snackbar.LENGTH_LONG).show()
            }
    }
}