package com.example.caripartner.data.repository

import android.util.Log
import com.example.caripartner.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {
    private lateinit var database: DatabaseReference

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

        val User = User(email=email,name= name, isAvailable =  isVerified, ktm = ktm, password = password, profil = "profil", preferences = listOf("1"))


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

//    fun GetUserLogin(uid: String, callback: (User) -> Unit) {
//        database = FirebaseDatabase.getInstance().getReference("Users")
//
//        // Get a reference to the user node in the database
//        val userRef = database.child(uid)
//
//        // Read data from the database
//        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val userData = snapshot.getValue(User::class.java)
//                callback(userData!!)
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Handle error
//                Log.e("UserRepository", "Error reading from database: ${error.message}")
//            }
//        })
//    }

    //    fun getAllUsersExceptCurrent(callback: (List<User>) -> Unit) {
//        // Assuming you have a way to get the current user's UID
//        val currentUserId = AuthRepository.getUserId()
//
//        database = FirebaseDatabase.getInstance().getReference("Users")
//
//        // Read all users from the database
//        database.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//            val users = mutableListOf<User>()
//
//            for (userSnapshot in snapshot.children) {
//                val user = userSnapshot.getValue(User::class.java)
//                if (user != null) {
//                    // Exclude the current user
//                    users.add(user)
//                }
//            }
//
//            callback(users)
//        }})
    fun getPartner(
        desiredPreferences: MutableList<String>,
        callback: (List<User>) -> Unit
    ) {
        val database = FirebaseDatabase.getInstance().reference.child("Users")

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val matchingUsers = snapshot.children.mapNotNull { userSnapshot ->
                    val user = userSnapshot.getValue(User::class.java)
                    val userPreferences = user?.preferences ?: emptyList()

                    user?.takeIf {
                        userPreferences.containsAll(desiredPreferences)
                    }
                }.take(20) // Ambil 20 pengguna pertama yang cocok dengan preferensi

                callback(matchingUsers)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                Log.e("UserRepository", "Error reading from database: ${error.message}")
            }
        })
    }


    fun GetUserLogin(uid: String, callback: (User) -> Unit) {
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


    fun GetUserPreferences(userId: String): MutableList<String> {
        database = FirebaseDatabase.getInstance().reference.child("Users").child(userId)
            .child("preferences")

        var preferences: MutableList<String> = mutableListOf()
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userPreferences =
                    snapshot.getValue(object : GenericTypeIndicator<ArrayList<String>>() {})
                // Lakukan sesuatu dengan daftar preferensi pengguna
                if (userPreferences != null) {
                    for ((index, preference) in userPreferences.withIndex()) {
                        // Lakukan sesuatu dengan setiap preferensi
                        preferences.add(preference)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        return preferences
    }


    fun GetUserRecommendation(
        desiredPreferences: MutableList<String>,
        Uid: String,
        callback: (List<User>) -> Unit
    ) {
        database = FirebaseDatabase.getInstance().reference.child("Users")

        var matchingUsers: List<User> = mutableListOf()

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentUser = Uid // Gantilah dengan UID pengguna saat ini

                val currentUserData = snapshot.child(currentUser).getValue(User::class.java)

                val filteredUsers = snapshot.children
                    .filter {
                        it.key != currentUser &&
                                !currentUserData!!.bookmark.contains(it.key) &&
                                !currentUserData!!.cancel.contains(it.key)
                    } // Hindari user saat ini
                    .filter {
                        val user = it.getValue(User::class.java)
                        val preferences = user?.preferences ?: emptyList()

                        // Lakukan filter berdasarkan preferensi dan hindari user yang terdaftar di bookmark atau cancel
                        user != null &&
                                preferences.containsAll(
                                    currentUserData?.preferences ?: emptyList() ?: emptyList()
                                )
                    }
                    .mapNotNull { it.getValue(User::class.java) }
                    .take(20)


                // Lakukan sesuatu dengan daftar filteredUsers
                matchingUsers = filteredUsers

                callback(filteredUsers)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    fun GetTargetUserIdByEmail(email: String, callback: (String) -> Unit) {
        val database = FirebaseDatabase.getInstance().reference.child("Users")

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)

                    // Cek apakah user tidak null dan email sesuai
                    if (user != null && user.email == email) {
                        val targetUserId = userSnapshot.key
                        println("Target User ID: $targetUserId")
                        callback(targetUserId!!)
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                println("Error: $error")
            }
        })
    }

    fun BookmarkUser(userId: String, bookmarkUserId: String) {
        val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

        // Mendapatkan referensi ke user yang akan ditambahkan bookmark
        val userRef = database.child(userId).child("bookmark")

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Mendapatkan nilai bookmark saat ini
                val currentBookmarks = snapshot.getValue<List<String>>()

                // Membuat atau menginisialisasi list jika belum ada
                val updatedBookmarks = if (currentBookmarks != null) {
                    currentBookmarks as MutableList<String>
                } else {
                    mutableListOf()
                }

                // Menambahkan user ke daftar bookmark
                updatedBookmarks.add(bookmarkUserId)

                // Memperbarui nilai bookmark di Firebase
                userRef.setValue(updatedBookmarks)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    fun CancelUser(userId: String, cancelUserId: String) {
        val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

        // Mendapatkan referensi ke user yang akan ditambahkan bookmark
        val userRef = database.child(userId).child("cancel")

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Mendapatkan nilai bookmark saat ini
                val currentCancels = snapshot.getValue<List<String>>()

                // Membuat atau menginisialisasi list jika belum ada
                val updatedCancels = if (currentCancels != null) {
                    currentCancels as MutableList<String>
                } else {
                    mutableListOf()
                }

                // Menambahkan user ke daftar bookmark
                updatedCancels.add(cancelUserId)

                // Memperbarui nilai bookmark di Firebase
                userRef.setValue(updatedCancels)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}
