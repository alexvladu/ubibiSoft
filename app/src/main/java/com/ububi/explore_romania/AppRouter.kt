package com.ububi.explore_romania

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ububi.explore_romania.ui.collection.CollectionRoute
import com.ububi.explore_romania.ui.gameboard.BoardScreen
import com.ububi.explore_romania.ui.home.HomeScreen
import com.ububi.explore_romania.ui.home.OpeningScreen


object Routes {
    const val OPENING = "opening"
    const val HOME = "home"
    const val GAME_BOARD = "gameboard"
    const val COLLECTION = "collection"
}

@Composable
fun AppRouter() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.OPENING) {
        composable(Routes.OPENING) {
            OpeningScreen(onFinished = {
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.OPENING) { inclusive = true }
                }
            })
        }

        composable(Routes.HOME) {
            HomeScreen(onPlayClick = { navController.navigate(Routes.GAME_BOARD) },
                onCollectionClick = { navController.navigate(Routes.COLLECTION) },
                onTreasureClick = { navController.navigate(Routes.GAME_BOARD) })
        }
        composable(Routes.GAME_BOARD) {
            BoardScreen(onBackClick = { navController.navigateUp() })
        }
        composable(Routes.COLLECTION) {
            CollectionRoute(
                onBackClick = { navController.navigate(Routes.HOME) }
            )
        }
    }
}