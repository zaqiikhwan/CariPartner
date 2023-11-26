package com.example.caripartner

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.caripartner.ui.screens.homeScreen.Home
import com.example.caripartner.ui.screens.loginScreen.LoginScreen
import com.example.caripartner.ui.screens.loginScreen.LoginViewModel
import com.example.caripartner.ui.screens.loginScreen.SignUpScreen
//import com.example.caripartner.ui.screens.recommendationScreen.Recommendation
import com.example.caripartner.ui.screens.homeScreen.HomeViewModel
import com.example.caripartner.ui.screens.profileScreen.ProfileViewModel
//import com.example.caripartner.ui.screens.recommendationScreen.RecommendationViewModel

enum class LoginRoutes{
    SignUp,
    SignIn
}

enum class HomeRoutes{
    Home,
}

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    loginViewModel: LoginViewModel,
    homeViewModel: HomeViewModel,
    profileViewModel: ProfileViewModel,
//    recommendationViewModel: RecommendationViewModel
){
    NavHost(
        navController = navController,
        startDestination = LoginRoutes.SignIn.name){
        composable(route = LoginRoutes.SignIn.name){
            LoginScreen(onNavToHomePage = {
                navController.navigate(HomeRoutes.Home.name){
                    launchSingleTop = true
                    popUpTo(route = LoginRoutes.SignIn.name){
                        inclusive = true
                    }
                }
            },
                loginViewModel = loginViewModel

            ) {
                navController.navigate(LoginRoutes.SignUp.name){
                    launchSingleTop = true
                    popUpTo(LoginRoutes.SignIn.name){
                        inclusive = true
                    }
                }
            }
        }

        composable(route = LoginRoutes.SignUp.name){
            SignUpScreen(onNavToHomePage = {
                navController.navigate(HomeRoutes.Home.name){
                    popUpTo(LoginRoutes.SignUp.name){
                        inclusive = true
                    }
                }
            },
                loginViewModel = loginViewModel
            ) {
                navController.navigate(LoginRoutes.SignIn.name)
            }
        }

        composable(route = HomeRoutes.Home.name){
//            Home(loginViewModel = loginViewModel)
//            Recommendation(recommendationViewModel = recommendationViewModel)
            Home(loginViewModel = loginViewModel,homeViewModel = homeViewModel,profileViewModel = profileViewModel)
        }

    }

}
