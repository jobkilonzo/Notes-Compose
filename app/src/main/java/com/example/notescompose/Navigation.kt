package com.example.notescompose

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.notescompose.detail.DetailScreen
import com.example.notescompose.detail.DetailViewModel
import com.example.notescompose.home.Home
import com.example.notescompose.home.HomeVIewModel
import com.example.notescompose.login.LoginScreen
import com.example.notescompose.login.LoginViewModel
import com.example.notescompose.login.SignUpScreen

enum class LoginRoutes{
    SignUp,
    SignIn
}
enum class HomeRoutes{
    Home,
    Detail
}

enum class NestedRoutes{
    Main,
    Login
}
@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    loginViewModel: LoginViewModel,
    detailViewModel: DetailViewModel,
    homeVIewModel: HomeVIewModel
) {
    NavHost(navController = navController,
        startDestination = NestedRoutes.Main.name
    ){
        authGraph(navController, loginViewModel)
        homeGraph(navController = navController, detailViewModel, homeVIewModel)

    }

}

fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel
){
    navigation(
        startDestination = LoginRoutes.SignIn.name,
        route = NestedRoutes.Login.name
    ){
        composable(route = LoginRoutes.SignIn.name){
            LoginScreen(onNavToHomePage = {
                navController.navigate(NestedRoutes.Main.name){
                    launchSingleTop = true
                    popUpTo(route = LoginRoutes.SignIn.name){
                        inclusive = true
                    }
                }
            }, loginViewModel = loginViewModel){
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
                navController.navigate(NestedRoutes.Main.name){
                    popUpTo(LoginRoutes.SignUp.name){
                        inclusive = true
                    }
                }
            }, loginViewModel = loginViewModel){
                navController.navigate(LoginRoutes.SignIn.name)
            }
        }
    }
}

fun NavGraphBuilder.homeGraph(
    navController: NavHostController,
    detailViewModel: DetailViewModel,
    homeVIewModel: HomeVIewModel
){
    navigation(
        startDestination = HomeRoutes.Home.name,
        route = NestedRoutes.Main.name
    ){
        composable(route = HomeRoutes.Home.name){
            Home(homeVIewModel = homeVIewModel,
                onNoteClick = { noteId ->
                navController.navigate(
                    HomeRoutes.Detail.name + "?id=$noteId"
                ){
                    launchSingleTop = true
                }
            },
                onNavToDetailPage = {
                    navController.navigate(HomeRoutes.Detail.name)
                }
            ){
                navController.navigate(NestedRoutes.Login.name){
                    launchSingleTop = true
                    popUpTo(0){
                        inclusive = true
                    }
                }
            }
        }

        composable(route = HomeRoutes.Detail.name + "?id={id}",
            arguments = listOf(navArgument("id"){
                type= NavType.StringType
                defaultValue = ""
            })
        ){
            entry ->
            DetailScreen(
                detailViewMdel = detailViewModel,
                noteId = entry.arguments?.getString("id") as String) {
                navController.navigateUp()

            }
        }
    }
}