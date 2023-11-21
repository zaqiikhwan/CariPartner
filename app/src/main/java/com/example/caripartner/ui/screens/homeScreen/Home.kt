package com.example.caripartner.ui.screens.homeScreen

//import androidx.compose.material.MaterialTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caripartner.ui.screens.loginScreen.LoginViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable

fun Home(loginViewModel: LoginViewModel?){
    val loginUiState = loginViewModel?.loginUiState
//    println(loginUiState)
    Text(text="This is home screen"+loginViewModel?.userId)

    Button(onClick = {
    val logout = Firebase.auth.signOut()
    logout },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 100.dp, top = 200.dp, bottom = 100.dp, end = 100.dp)
            .background(color = Color(0xFF4B4EFC), shape = RoundedCornerShape(size = 12.dp)),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B4EFC))
    ) {
        Text(text = "Logout",
            fontSize = 18.sp,
            lineHeight = 28.sp,
            fontWeight = FontWeight(600),
            color = Color(0xFFFFFFFF),)
    }
}

// Obtain an instance of FirebaseAuth
//val firebaseAuth = FirebaseAuth.getInstance()
//val logout = Firebase.auth.signOut()






