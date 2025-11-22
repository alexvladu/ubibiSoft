package com.ububi.explore_romania

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ububi.explore_romania.ui.gameboard.BoardScreen
import com.ububi.explore_romania.ui.home.HomeScreen


object Routes {
    const val HOME = "home"
    const val GAME_BOARD = "gameboard"
}


@Composable
fun AppRouter() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(onStartClick = { navController.navigate(Routes.GAME_BOARD) })
        }
        composable(Routes.GAME_BOARD) {
            BoardScreen(onBackClick = { navController.navigateUp() })
        }
    }
}