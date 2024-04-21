package com.vasberc.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.vasberc.presentation.navigation.BookFlixRoutes

@Composable
fun CurrentDestinationHandler(
    navController: NavHostController,
    onDestinationResolved: (title: String, hasBackButton: Boolean) -> Unit
) {

    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = remember(currentBackStack) {
        currentBackStack?.destination
    }

    if(currentDestination?.route == BookFlixRoutes.BookList.route) {
        onDestinationResolved("All books", false)
    } else if(currentDestination?.route == BookFlixRoutes.BookDetailedScreen.route) {
        val title = currentBackStack?.arguments?.getString("title", "") ?: ""
        onDestinationResolved(title, true)
    }

}