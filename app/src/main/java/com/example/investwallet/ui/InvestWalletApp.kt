package com.example.investwallet.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.investwallet.ui.detail.DetailNews
import com.example.investwallet.ui.detail.DetailScreen
import com.example.investwallet.ui.home.Home
import com.example.investwallet.ui.search.SearchScreen


sealed class Screen(val route: String){
    object Home: Screen("home")
    object Search: Screen("search")
    object Detail: Screen("detail")
    object DetailNews: Screen("detail_news")
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
                },
                onDetail = {
                    navController.navigate(Screen.Detail.route){
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.Search.route){
            SearchScreen(
                onBack = {
                    navController.popBackStack()
                },
                onOpen = {
                    tag: String, category: String ->
                    navController.navigate(
                        "${Screen.Detail.route}?tag=$tag&category=$category"
                    ){
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = "${Screen.Detail.route}?tag={tag}&category={category}",
            arguments = listOf(
                navArgument("tag"){ type = NavType.StringType },
                navArgument("category"){ type = NavType.StringType },
            )
        ){
                backStackEntry ->

            DetailScreen(
                onBack = {
                    navController.popBackStack()
                },
                onClick = {
                    navController.navigate(Screen.DetailNews.route)
                },
                tagTicket = backStackEntry.arguments?.getString("tag", " ")!!,
                category = backStackEntry.arguments?.getString("category", " ")!!,
            )
        }

        composable(
            route = Screen.DetailNews.route
        ){
            DetailNews(
                onBack = {
                    navController.popBackStack()
                },
                onClick = {
                    navController.navigate("${Screen.Detail.route}?tag=$it")
                }
            )
        }
    }

}