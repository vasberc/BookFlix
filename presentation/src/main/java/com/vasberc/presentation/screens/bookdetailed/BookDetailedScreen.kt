package com.vasberc.presentation.screens.bookdetailed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import org.koin.androidx.compose.getViewModel

@Composable
fun BookDetailedScreen(
    navHostController: NavHostController,
    onMessage: (String) -> Unit,
    viewModel: BookDetailedViewModel = getViewModel()
) {

    if(viewModel.bookDetailedScreenState.message != null) {
        LaunchedEffect(null) {
            onMessage(viewModel.bookDetailedScreenState.message!!)
            navHostController.popBackStack()
        }
    }
}

@Composable
fun BookDetailedScreenMainContent(

) {

}

@Composable
@Preview
fun PreviewBookDetailedScreenMainContent() {

}