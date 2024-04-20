package com.vasberc.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vasberc.presentation.navigation.BookFlixNavHost

@Composable
fun HomeScreen() {

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        BookFlixNavHost(
            modifier = Modifier.padding(paddingValues)
        )
    }
}