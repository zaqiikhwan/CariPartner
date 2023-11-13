package com.example.caripartner.login

import android.widget.GridLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caripartner.login.LoginViewModel
import com.example.caripartner.R
import com.example.caripartner.ui.theme.CariPartnerTheme
import java.time.format.TextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel? = null,
    onNavToHomePage:() -> Unit,
    onNavToSignUpPage:() -> Unit,
){
    val loginUiState = loginViewModel?.loginUiState
    val isError = loginUiState?.loginError!= null
    val context = LocalContext.current


    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start,
    ){
        if(isError){
            Text(
                text = loginUiState?.loginError ?: "Unknown error",
                color = Color.Red,
            )
        }

        Text(
            text = "Selamat Datang",
            fontSize = 18.sp,
            lineHeight = 28.sp,
            fontWeight = FontWeight(600),
            color = Color(0xFF101828),
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
            textAlign = TextAlign.Right,
        )

        Text(
            text = "Masuk untuk menjelajah lebih jauh",
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 16.dp, bottom = 38.dp),
            lineHeight = 18.sp,
            fontWeight = FontWeight(400),
            color = Color(0xFF667085),
            textAlign = TextAlign.Right,
        )


        Image(
            painter = painterResource(id = R.drawable.pana),
            modifier = Modifier
                .fillMaxWidth()
                .width(153.66031.dp)
                .height(150.dp),
            contentDescription = ""
        )

        Text(
            text = "Email",
            modifier = Modifier.padding(start=16.dp, top=30.dp),
            fontSize = 12.sp,
            lineHeight = 12.sp,
            fontWeight = FontWeight(600),
            color = Color(0xFF101828)
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            value = loginUiState?.userName ?: "",
            onValueChange = {loginViewModel?.onUserNameChange(it)},
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Default.Person,
//                    contentDescription = null,
//                )
//            },
            label = {
                Text(
                    text = "Masukkan Email",
                    fontSize = 14.sp
                )
            },
            isError = isError
        )

        Text(
            text = "Password",
            modifier = Modifier.padding(start=16.dp, top=16.dp),
            fontSize = 12.sp,
            lineHeight = 12.sp,
            fontWeight = FontWeight(600),
            color = Color(0xFF101828)
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            value = loginUiState?.password ?: "",
            onValueChange = {loginViewModel?.onPasswordChange(it)},
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Default.Person,
//                    contentDescription = null,
//                )
//            },
            label = {
                Text(
                    text = "Masukkan Password",
                    fontSize = 14.sp
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            isError = isError
        )

        Button(onClick = { loginViewModel?.loginUser(context) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 40.dp, bottom = 40.dp, end = 16.dp)
                .background(color = Color(0xFF4B4EFC), shape = RoundedCornerShape(size = 8.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B4EFC))
        ) {
            Text(text = "Login",
                fontSize = 18.sp,
                lineHeight = 28.sp,
                fontWeight = FontWeight(600),
                color = Color(0xFFFFFFFF),)
        }

        Row (modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ){
            Text(text = "Belum memiliki akun? ",
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight(600),
                color = Color(0xFF101828),)
            Spacer(modifier=Modifier.size(8.dp))
            TextButton(onClick = { onNavToSignUpPage.invoke() }) {
                Text(text = "Daftar")
            }
        }

        if(loginUiState?.isLoading == true){
            CircularProgressIndicator()
        }

        LaunchedEffect(key1 = loginViewModel?.hasUser){
            if(loginViewModel?.hasUser == true){
                onNavToHomePage.invoke()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    loginViewModel: LoginViewModel? = null,
    onNavToHomePage:() -> Unit,
    onNavToLoginPage:() -> Unit,
){
    val loginUiState = loginViewModel?.loginUiState
    val isError = loginUiState?.signUpError!= null
    val context = LocalContext.current


    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start,
    ){
        if(isError){
            Text(
                text = loginUiState?.signUpError ?: "Unknown error",
                color = Color.Red,
            )
        }

        Text(
            text = "Halo!",
            fontSize = 18.sp,
            lineHeight = 28.sp,
            fontWeight = FontWeight(600),
            color = Color(0xFF101828),
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
            textAlign = TextAlign.Right,
        )

        Text(
            text = "Buat akun untuk menjelajah lebih jauh",
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 16.dp, bottom = 38.dp),
            lineHeight = 18.sp,
            fontWeight = FontWeight(400),
            color = Color(0xFF667085),
            textAlign = TextAlign.Right,
        )

        Text(
            text = "Email",
            modifier = Modifier.padding(start=16.dp, top=30.dp),
            fontSize = 12.sp,
            lineHeight = 12.sp,
            fontWeight = FontWeight(600),
            color = Color(0xFF101828)
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            value = loginUiState?.userNameSignUp ?: "",
            onValueChange = {loginViewModel?.onUserNameChangeSignUp(it)},
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Default.Person,
//                    contentDescription = null,
//                )
//            },
            label = {
                Text(
                    text = "Masukkan Email",
                    fontSize = 14.sp
                )
            },
            isError = isError
        )

        Text(
            text = "Password",
            modifier = Modifier.padding(start=16.dp, top=16.dp),
            fontSize = 12.sp,
            lineHeight = 12.sp,
            fontWeight = FontWeight(600),
            color = Color(0xFF101828)
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            value = loginUiState?.passwordSignUp ?: "",
            onValueChange = {loginViewModel?.onPasswordChangeSignUp(it)},
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Default.Person,
//                    contentDescription = null,
//                )
//            },
            label = {
                Text(
                    text = "Masukkan Password",
                    fontSize = 14.sp
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            isError = isError
        )

        Text(
            text = "Konfirmasi Password",
            modifier = Modifier.padding(start=16.dp, top=16.dp),
            fontSize = 12.sp,
            lineHeight = 12.sp,
            fontWeight = FontWeight(600),
            color = Color(0xFF101828)
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            value = loginUiState?.confirmPasswordSignUp ?: "",
            onValueChange = {loginViewModel?.onConfirmPasswordChange(it)},
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Default.Person,
//                    contentDescription = null,
//                )
//            },
            label = {
                Text(
                    text = "Masukkan Password",
                    fontSize = 14.sp
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            isError = isError
        )

        Button(onClick = { loginViewModel?.createUser(context) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 40.dp, bottom = 40.dp, end = 16.dp)
                .background(color = Color(0xFF4B4EFC), shape = RoundedCornerShape(size = 8.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B4EFC))
        ) {
            Text(text = "Buat Akun",
                fontSize = 18.sp,
                lineHeight = 28.sp,
                fontWeight = FontWeight(600),
                color = Color(0xFFFFFFFF),)
        }

        Row (modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ){
            Text(text = "Sudah memiliki akun? ",
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight(600),
                color = Color(0xFF101828),)
            Spacer(modifier=Modifier.size(8.dp))
            TextButton(onClick = { onNavToLoginPage.invoke() }) {
                Text(text = "Login")
            }
        }

        if(loginUiState?.isLoading == true){
            CircularProgressIndicator()
        }

        LaunchedEffect(key1 = loginViewModel?.hasUser){
            if(loginViewModel?.hasUser == true){
                onNavToHomePage.invoke()
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PrevLoginScreen(){
    CariPartnerTheme {
        LoginScreen(onNavToHomePage = { /*TODO*/ }) {
            
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun PrevSignUpScreen(){
    CariPartnerTheme {
        SignUpScreen(onNavToHomePage = { /*TODO*/ }) {
            
        }
    }
}