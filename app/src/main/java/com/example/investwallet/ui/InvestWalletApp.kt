package com.example.investwallet.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.investwallet.ui.home.Home
import com.example.investwallet.ui.search.SearchScreen


sealed class Screen(val route: String){
    object Home: Screen("home")
    object Search: Screen("search")
}

@Composable
fun InvestWalletApp(

) {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Home.route){
        composable(Screen.Home.route){
            Home(
                onSearch = {
                    navController.navigate(Screen.Search.route){
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.Search.route){
            SearchScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }

}