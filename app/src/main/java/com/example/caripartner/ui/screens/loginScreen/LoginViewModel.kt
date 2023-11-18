package com.example.caripartner.ui.screens.loginScreen

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caripartner.data.repository.AuthRepository
import com.example.caripartner.data.repository.StorageRepository
import com.example.caripartner.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val AuthRepository: AuthRepository = AuthRepository(),
    private val UserRepository: UserRepository = UserRepository(),
    private val StorageRepository: StorageRepository = StorageRepository()
):ViewModel() {

    val currentUser = AuthRepository.currentUser

    val userId:String
        get() = AuthRepository.getUserId()

    val hasUser:Boolean
        get() = AuthRepository.isLogin()

    var loginUiState by mutableStateOf(LoginUiState())
        private set

    fun onEmailChange(email:String){
        loginUiState = loginUiState.copy(email = email)
    }

    fun onEmailSignUpChange(email:String){
        loginUiState = loginUiState.copy(emailSignUp = email)
    }

    fun onKtmChange(bitmap:Bitmap, imageUri:Uri, img: String){
        loginUiState = loginUiState.copy(bitmap = bitmap)
        loginUiState = loginUiState.copy(img = img)
        loginUiState = loginUiState.copy(imageUri = imageUri)
    }

    fun onPasswordChange(password:String){
        loginUiState = loginUiState.copy(password = password)
    }

    fun onUserNameChangeSignUp(userName:String){
        loginUiState = loginUiState.copy(userNameSignUp = userName)
    }

    fun onPasswordChangeSignUp(password:String){
        loginUiState = loginUiState.copy(passwordSignUp = password)
    }

    fun onConfirmPasswordChange(password:String){
        loginUiState = loginUiState.copy(confirmPasswordSignUp = password)
    }

    private fun validateLoginForm()=
        loginUiState.email.isNotBlank() &&
                loginUiState.password.isNotBlank()

    private fun validateSignUpForm() =
        loginUiState.userNameSignUp.isNotBlank() &&
                loginUiState.passwordSignUp.isNotBlank() &&
                loginUiState.confirmPasswordSignUp.isNotBlank()

    fun  createUser(context: Context) = viewModelScope.launch {
        try {
            if(!loginUiState.userNameSignUp.isNotBlank()){
                throw IllegalArgumentException("Nama harus diisi")
            }
            if(!validateSignUpForm()){
                throw IllegalArgumentException("Email dan Password harus diisi")
            }
            if(loginUiState.passwordSignUp!=loginUiState.confirmPasswordSignUp){
                throw IllegalArgumentException("Konfirmasi password tidak sama")
            }
            if(loginUiState.imageUri==null){
                throw IllegalArgumentException("Upload KTM anda terlebih dahulu")
            }
            loginUiState = loginUiState.copy(isLoading = true)

            loginUiState = loginUiState.copy(signUpError = null)


            AuthRepository.createUser(
                loginUiState.emailSignUp,
                loginUiState.passwordSignUp
            ){ isSucessful ->
                if(isSucessful){
                    UserRepository.CreateUser(loginUiState.emailSignUp,loginUiState.userNameSignUp,false,loginUiState.img,loginUiState.passwordSignUp,AuthRepository.getUserId()) { success ->
                        if(success){
                            Toast.makeText(
                                context,
                                "Success Create User",
                                Toast.LENGTH_SHORT
                            ).show()
                        }else{
                            Toast.makeText(
                                context,
                                "Failed Create User",
                                Toast.LENGTH_SHORT
                            ).show()
                            loginUiState = loginUiState.copy(isSuccessLogin = false)
                        }
                    }
                    StorageRepository.uploadImageToFirebase(loginUiState.bitmap!!,context as ComponentActivity) { success ->
                        if(success){
                            Toast.makeText(
                                context,
                                "Success Upload KTM",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                    Toast.makeText(
                        context,
                        "Success Login",
                        Toast.LENGTH_SHORT
                    ).show()
                    loginUiState = loginUiState.copy(isSuccessLogin = true)
                }else{
                    Toast.makeText(
                        context,
                        "Failed Login",
                        Toast.LENGTH_SHORT
                    ).show()
                    loginUiState = loginUiState.copy(isSuccessLogin = false)
                }
            }
        }catch(e:Exception){
            loginUiState = loginUiState.copy(signUpError = e.localizedMessage)
            e.printStackTrace()
        }finally {
            loginUiState = loginUiState.copy(isLoading = false)
        }
    }


    fun  loginUser(context: Context) = viewModelScope.launch {
        try {
            if(!validateLoginForm()){
                throw IllegalArgumentException("Email and password can not be empty")
            }
            loginUiState = loginUiState.copy(isLoading = true)
            loginUiState = loginUiState.copy(loginError = null)
            AuthRepository.login(
                loginUiState.email,
                loginUiState.password
            ){ isSucessful ->
                if(isSucessful){
                    Toast.makeText(
                        context,
                        "Success Login",
                        Toast.LENGTH_SHORT
                    ).show()
                    loginUiState = loginUiState.copy(isSuccessLogin = true)
                }else{
                    Toast.makeText(
                        context,
                        "Failed Login",
                        Toast.LENGTH_SHORT
                    ).show()
                    loginUiState = loginUiState.copy(isSuccessLogin = false)
                }
            }
        }catch(e:Exception){
            loginUiState = loginUiState.copy(loginError = e.localizedMessage)
            e.printStackTrace()
        }finally {
            loginUiState = loginUiState.copy(isLoading = false)
        }
    }
}

data class LoginUiState(
    val email:String = "",
    val password:String = "",
    val emailSignUp:String = "",
    val userNameSignUp:String = "",
    val passwordSignUp:String = "",
    val confirmPasswordSignUp:String = "",
    val isLoading:Boolean = false,
    val isSuccessLogin:Boolean = false,
    val signUpError:String? = null,
    val loginError:String? = null,
    val bitmap: Bitmap? =null,
    val img: String ="",
    var imageUri: Uri? = null
)