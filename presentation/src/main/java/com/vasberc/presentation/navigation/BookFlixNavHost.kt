package com.vasberc.presentation.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun BookFlixNavHost(
    modifier: Modifier = Modifier
) {

    val navController = rememberNavController()

    NavHost(navController = navController,  startDestination = BookFlixRoutes.BookList.route, modifier = modifier) {
        composable(BookFlixRoutes.BookList.route) {
            Text(text = "Hello book list")
        }

        composable(BookFlixRoutes.BookDetailedScreen.route, BookFlixRoutes.BookDetailedScreen.arguments) {

        }
    }
}

sealed class BookFlixRoutes(val route: String) {
    data object BookList : BookFlixRoutes("BookList")
    data object BookDetailedScreen : BookFlixRoutes("BookDetailedScreen/{bookId}") {
        val arguments = listOf(
            navArgument("bookId") {
                type = NavType.IntType
                defaultValue = -1
            }
        )
    }
}