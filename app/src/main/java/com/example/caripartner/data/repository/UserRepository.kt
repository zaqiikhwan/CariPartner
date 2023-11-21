package com.example.caripartner.data.repository

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.caripartner.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.core.UserData
import kotlinx.coroutines.tasks.await

class UserRepository {
    private lateinit var database: DatabaseReference
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun CreateUser(
        email: String,
        name: String,
        isVerified: Boolean,
        ktm: String,
        password: String,
        Uid: String,
        callback: (Boolean) -> Unit
    ) {
        database = FirebaseDatabase.getInstance().getReference("Users")

        val User = User(email, name, isVerified, ktm, password)


        database.child(Uid).setValue(User).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Operasi penulisan berhasil
                callback(true)
            } else {
                // Operasi penulisan gagal
                callback(false)
                // Dapatkan pesan kesalahan dari task.getException() dan tampilkan atau log sesuai kebutuhan
                val error = task.exception?.message
                Log.e("UserRepository", "Error writing to database: $error")
            }
        }
    }
}