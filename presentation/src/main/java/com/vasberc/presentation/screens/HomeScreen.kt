package com.vasberc.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.vasberc.presentation.components.CurrentDestinationHandler
import com.vasberc.presentation.components.TopBar
import com.vasberc.presentation.navigation.BookFlixNavHost
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {

    val message by remember { mutableStateOf<String?>(null) }
    var title by remember { mutableStateOf<String?>(null) }
    var hasBackButton by remember { mutableStateOf(false) }

    val navController = rememberNavController()

    CurrentDestinationHandler(navController = navController) { currentTitle, hasBack ->
        title = currentTitle
        hasBackButton = hasBack
    }

    Scaffold(
        topBar = {
            TopBar(
                title = title,
                hasBackButton = hasBackButton
            ) {
                navController.popBackStack()
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        BookFlixNavHost(
            modifier = Modifier.padding(paddingValues),
            navController =  navController
        )




        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()
        LaunchedEffect(message) {
            coroutineScope.launch {
                if(message != null) {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(message = message!!, duration = SnackbarDuration.Short)
                }
            }
        }
    }
}