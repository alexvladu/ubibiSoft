package com.ububi.explore_romania

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ububi.explore_romania.ui.chest.ChestScreen
import com.ububi.explore_romania.ui.gameboard.BoardScreen
import com.ububi.explore_romania.ui.home.HomeScreen
import com.ububi.explore_romania.ui.home.OpeningScreen
import com.ububi.explore_romania.ui.quiz.QuizScreen
object Routes {
    const val OPENING = "opening"
    const val HOME = "home"
    const val GAME_BOARD = "gameboard"

    const val CHEST="chest"
    const val QUIZ = "quiz"
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
                onCollectionClick = { navController.navigate(Routes.GAME_BOARD) },
                onTreasureClick = { navController.navigate(Routes.CHEST) })
        }
        composable(Routes.GAME_BOARD) {
            BoardScreen(navController = navController)
        }
        composable(Routes.QUIZ) {
            QuizScreen(navController = navController)
        }
        composable(Routes.CHEST) {
            ChestScreen(navController=navController)
        }
    }
}