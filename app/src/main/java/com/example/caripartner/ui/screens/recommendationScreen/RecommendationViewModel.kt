package com.example.caripartner.ui.screens.recommendationScreen

import androidx.lifecycle.ViewModel
import com.example.caripartner.data.model.User
import com.example.caripartner.data.repository.AuthRepository
import com.example.caripartner.data.repository.StorageRepository
import com.example.caripartner.data.repository.UserRepository


class RecommendationViewModel(
    private val AuthRepository: AuthRepository = AuthRepository(),
    private val UserRepository: UserRepository = UserRepository(),
    private val StorageRepository: StorageRepository = StorageRepository()
): ViewModel()  {
    fun GetRecommendation(callback: (List<User>) -> Unit){
        UserRepository.GetUserRecommendation(UserRepository.GetUserPreferences(AuthRepository.getUserId()),AuthRepository.getUserId()){matchingUsers ->
            callback(matchingUsers)
        }

        UserRepository.GetTargetUserIdByEmail("ikhwankiram10@gmail.com"){targetUserId ->
            print("++++++++++++++++++++++++++")
            println(targetUserId)
        }
    }

    fun GetUserData( callback:(User)-> Unit){
        UserRepository.GetUserLogin(AuthRepository.getUserId()){user ->
            callback(user)
        }
    }

    fun getImageUrl(fileName:String,imageUrl: (String) -> Unit){
        StorageRepository.getUserProfileImage(fileName){url->
            imageUrl(url)
        }
    }

    fun AddBookMark(email:String){
        println("GGGGGGGGGGGGGGGG")
        UserRepository.GetTargetUserIdByEmail(email){id->
            UserRepository.BookmarkUser(userId = AuthRepository.getUserId(),bookmarkUserId=id?:"")
        }
    }

    fun AddCancel(email: String){
        UserRepository.GetTargetUserIdByEmail(email){id->
            UserRepository.CancelUser(userId = AuthRepository.getUserId(),cancelUserId=id?:"")
        }
    }
}