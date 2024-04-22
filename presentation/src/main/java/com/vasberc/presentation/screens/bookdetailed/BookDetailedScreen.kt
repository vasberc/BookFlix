package com.vasberc.presentation.screens.bookdetailed

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vasberc.domain.model.BookDetailed
import com.vasberc.presentation.R
import com.vasberc.presentation.components.shimmerBrush
import org.koin.androidx.compose.getViewModel
import kotlin.math.abs

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
    } else if(viewModel.bookDetailedScreenState.loading) {

    } else if(viewModel.bookDetailedScreenState.data != null) {
        BookDetailedScreenMainContent(viewModel.bookDetailedScreenState.data!!)
    }
}

@Composable
fun BookDetailedScreenMainContent(book: BookDetailed) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
    ) {

        Spacer(modifier = Modifier.height(1.dp))

        var showShimmer by remember { mutableStateOf(true) }

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(book.image)
                .crossfade(true)
                .build(),
            contentDescription = "Book ${book.title} cover image",
            error = painterResource(id = R.drawable.ic_no_image_placeholder),
            onError = { showShimmer = false },
            onSuccess = { showShimmer = false },
            modifier = Modifier
                .height(250.dp)
                .clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
                .fillMaxWidth()
                .background(shimmerBrush(showShimmer = showShimmer, targetValue = 4000f)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Title",
            modifier = Modifier.padding(horizontal = 10.dp),
            style = TextStyle(
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )

        Text(
            text = book.title,
            modifier = Modifier.padding(horizontal = 10.dp),
            style = TextStyle(
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Subject",
            modifier = Modifier.padding(horizontal = 10.dp),
            style = TextStyle(
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )

        Text(
            text = book.subject,
            modifier = Modifier.padding(horizontal = 10.dp),
            style = TextStyle(
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Author" + if(book.authors.size != 1) "s" else "",
            modifier = Modifier.padding(horizontal = 10.dp),
            style = TextStyle(
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )

        Text(
            text = book.authors.joinToString(" &\n") {
                "- ${it.name}" +
                        if(it.birthYear == -1 && it.deathYear == -1) {
                            ""
                        } else {
                            //BC handling
                            val birth = if(it.birthYear < 0) abs(it.birthYear).toString() + " BC" else it.birthYear.toString()
                            val death = if(it.deathYear < 0) abs(it.deathYear).toString() + " BC" else it.deathYear.toString()
                            " ($birth - $death)"
                        }
            },
            modifier = Modifier.padding(horizontal = 10.dp),
            style = TextStyle(
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

    }
}

@Composable
@Preview
fun PreviewBookDetailedScreenMainContent() {

}