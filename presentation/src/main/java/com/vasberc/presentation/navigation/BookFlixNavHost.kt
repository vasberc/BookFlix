package com.vasberc.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vasberc.presentation.screens.bookdetailed.BookDetailedScreen
import com.vasberc.presentation.screens.booklist.BookListScreen

@Composable
fun BookFlixNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onMessage: (message: String) -> Unit
) {

    NavHost(navController = navController,  startDestination = BookFlixRoutes.BookList.route, modifier = modifier) {
        composable(BookFlixRoutes.BookList.route) {
            BookListScreen(navController)
        }

        composable(BookFlixRoutes.BookDetailedScreen.route, BookFlixRoutes.BookDetailedScreen.arguments) {
            BookDetailedScreen(navController, onMessage)
        }
    }
}

sealed class BookFlixRoutes(val route: String) {
    data object BookList : BookFlixRoutes("BookList")
    data object BookDetailedScreen : BookFlixRoutes("BookDetailedScreen?bookId={bookId}&title={title}") {
        val arguments = listOf(
            navArgument("bookId") {
                type = NavType.IntType
                defaultValue = -1
            },
            navArgument("title") {
                type = NavType.StringType
                defaultValue = ""
            }
        )
    }
}