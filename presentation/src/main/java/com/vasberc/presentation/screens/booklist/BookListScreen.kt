package com.vasberc.presentation.screens.booklist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.vasberc.domain.model.BookItem
import com.vasberc.presentation.components.BookListItem
import com.vasberc.presentation.navigation.BookFlixRoutes
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import timber.log.Timber
import kotlin.math.roundToInt

@Composable
fun BookListScreen(
    navController: NavHostController,
    viewModel: BookListViewModel = getViewModel()
) {

    BookListMainContent(
        books = viewModel.pagerFlow,
        onNavigateToDetailedView = {
            navController.navigate(
                BookFlixRoutes.BookDetailedScreen.route
                    .replace(
                        "{bookId}",
                        it.id.toString()
                    )
                    .replace(
                        "{title}",
                        it.title
                    )
            )
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListMainContent(
    books: Flow<PagingData<BookItem>>,
    onNavigateToDetailedView: (BookItem) -> Unit
) {
    //This variable holds the retry job in order to be able to know that is started and to cancel it
    var retryJob by remember {
        mutableStateOf<Job?>(null)
    }
    val lazyPagingItems = books.collectAsLazyPagingItems()
    val state = rememberPullToRefreshState()
    if (state.isRefreshing) {
        LaunchedEffect(true) {
            retryJob?.cancel()
            retryJob = null
            lazyPagingItems.refresh()
        }
    }

    val lifeCycleOwner = LocalLifecycleOwner.current

    Box(
        Modifier
            .fillMaxSize()
            .nestedScroll(state.nestedScrollConnection)) {

        val scrollState = rememberLazyListState()

        Timber.d("paging items count ${lazyPagingItems.itemCount}")
        LazyColumn(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .padding(PaddingValues(5.dp, 0.dp))
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = scrollState
        ) {
            items(lazyPagingItems.itemCount) { index ->
                key(lazyPagingItems[index]?.id ?: (0 - index)) {
                    Spacer(modifier = Modifier.height(5.dp))
                    BookListItem(
                        item = lazyPagingItems[index],
                        onItemClicked = {
                            if(lifeCycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                                onNavigateToDetailedView(it)
                            }
                        }
                    )
                    if (lazyPagingItems.itemCount - 1 == index) {
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
            }
        }

        val scrollCoroutineScope = rememberCoroutineScope()

        val fabIsVisible by remember { derivedStateOf { scrollState.firstVisibleItemIndex  >= 2 } }

        val buttonPadding = 20.dp
        val pxValue = with(LocalDensity.current) { buttonPadding.toPx() }.roundToInt()

        AnimatedVisibility(
            enter = slideInVertically(
                initialOffsetY = {
                    //To start out of the screen the offset is set to the height of the component +
                    //the padding value
                    it + pxValue
                },
            ),
            exit = slideOutVertically(
                targetOffsetY = {
                    //Same with initial starting logic to go out of the screen
                    it + pxValue
                },
            ),
            visible = fabIsVisible,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(buttonPadding)
        ) {
            FloatingActionButton(
                modifier = Modifier
                    .size(50.dp, 50.dp),
                onClick = {
                    scrollCoroutineScope.launch {
                        scrollState.scrollToItem(0)
                    }
                },
                containerColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowUp,
                    contentDescription = "Press to scroll to top",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(25.dp)
                )
            }
        }

        PullToRefreshContainer(
            modifier = Modifier.align(Alignment.TopCenter),
            state = state,
        )

        val retryCoroutineScope = rememberCoroutineScope()
        var startJob by remember {
            mutableStateOf(false)
        }

        when (lazyPagingItems.loadState.mediator?.refresh) {
            is LoadState.Error -> {
                state.endRefresh()
                if(retryJob == null) {
                    startJob = true
                }
            }
            is LoadState.NotLoading -> {
                state.endRefresh()
                if(lazyPagingItems.loadState.append is LoadState.Error) {
                    if(retryJob == null) {
                        startJob = true
                    }
                } else {
                    retryJob?.cancel()
                    retryJob = null
                }
            }
            else -> Unit
        }

        if(startJob) {
            startJob = false
            LaunchedEffect(key1 = null) {
                var retries = 0
                retryJob = retryCoroutineScope.launch {
                    while (retries < 5) {
                        delay(2000)
                        lazyPagingItems.retry()
                    }
                }
                retryJob!!.invokeOnCompletion {
                    Timber.w(it, "Retry completed")
                }
            }
        }

    }
}

@Composable
@Preview
fun PreviewBookListMainContent() {

}