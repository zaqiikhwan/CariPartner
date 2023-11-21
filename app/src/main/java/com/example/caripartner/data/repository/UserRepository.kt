package com.example.caripartner.data.repository

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.caripartner.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.core.UserData
import kotlinx.coroutines.tasks.await

class UserRepository {
    private lateinit var database: DatabaseReference
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val AuthRepository: AuthRepository = AuthRepository()

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

    fun GetUserLogin(uid: String, callback:(User)-> Unit){
        database = FirebaseDatabase.getInstance().getReference("Users")

        // Get a reference to the user node in the database
        val userRef = database.child(uid)

        // Read data from the database
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.getValue(User::class.java)
                callback(userData!!)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                Log.e("UserRepository", "Error reading from database: ${error.message}")
            }
        })
    }

    fun getAllUsersExceptCurrent(callback: (List<User>) -> Unit) {
        // Assuming you have a way to get the current user's UID
        val currentUserId = AuthRepository.getUserId()

        database = FirebaseDatabase.getInstance().getReference("Users")

        // Read all users from the database
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = mutableListOf<User>()

                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    if (user != null) {
                        // Exclude the current user
                        users.add(user)
                    }
                }

                callback(users)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                Log.e("UserRepository", "Error reading from database: ${error.message}")
            }
        })
    }

}