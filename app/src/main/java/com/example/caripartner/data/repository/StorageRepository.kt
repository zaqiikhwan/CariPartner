package com.example.caripartner.data.repository

import android.graphics.Bitmap
import androidx.activity.ComponentActivity
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import java.io.ByteArrayOutputStream

class StorageRepository {
    fun uploadImageToFirebase(bitmap: Bitmap, context: ComponentActivity, callback: (Boolean)->Unit) {
        val storageRef = Firebase.storage.reference
        val imageRef = storageRef.child("ktm/${bitmap}")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val imageData = baos.toByteArray()
        imageRef.putBytes(imageData).addOnSuccessListener {
            callback(true)
        }.addOnFailureListener{
            callback(false)
        }
    }
}