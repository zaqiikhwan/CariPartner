package com.example.caripartner.ui.screens.homeScreen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.caripartner.ui.screens.loginScreen.LoginViewModel

@Composable

fun Home(loginViewModel: LoginViewModel?){
    val loginUiState = loginViewModel?.loginUiState
    Text(text="This is home screen"+loginViewModel?.userId)
}