package com.vasberc.presentation.screens.booklist

import androidx.compose.foundation.background
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.vasberc.domain.model.BookItem
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.getViewModel
import timber.log.Timber

@Composable
fun BookListScreen(
    navController: NavHostController,
    onMessage: (message: String) -> Unit,
    viewModel: BookListViewModel = getViewModel()
) {

    BookListMainContent(
        viewModel.pagerFlow
    )

}

@Composable
fun BookListMainContent(
    books: Flow<PagingData<BookItem>>
) {

    val lazyPagingItems = books.collectAsLazyPagingItems()
    val scrollState = rememberLazyListState()

    when (lazyPagingItems.loadState.refresh) {
        is LoadState.Loading -> {
//            onLoadingChange(true)
        }

        is LoadState.Error -> {
//            onLoadingChange(false)
        }

        else -> {
//            onLoadingChange(false)
            Timber.d("paging items count ${lazyPagingItems.itemCount}")
            if (lazyPagingItems.itemCount > 0) {
                LazyColumn(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .padding(PaddingValues(5.dp, 0.dp))
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    state = scrollState
                ) {
                    items(lazyPagingItems.itemCount) { index ->
                        key(lazyPagingItems[index]?.id ?: (0 - index).toLong()) {
                            if (lazyPagingItems.loadState.prepend is LoadState.Loading && index == 0) {
                                Spacer(modifier = Modifier.height(3.dp))
                                CircularProgressIndicator(
                                    modifier = Modifier.size(size = 50.dp),
                                    strokeWidth = 8.dp,
                                )
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(text = lazyPagingItems[index]?.title ?: "loading")
                            if (lazyPagingItems.loadState.append is LoadState.Loading && index == lazyPagingItems.itemCount - 1) {
                                Spacer(modifier = Modifier.height(3.dp))
                                CircularProgressIndicator(
                                    modifier = Modifier.size(size = 50.dp),
                                    strokeWidth = 8.dp,
                                )
                            }
                            if (lazyPagingItems.itemCount - 1 == index) {
                                Spacer(modifier = Modifier.height(5.dp))
                            }
                        }
                    }
                }
            } else if (lazyPagingItems.loadState.append.endOfPaginationReached) {
//                EmptyStateWithImage(
//                    messageId = R.string.no_transactions,
//                    drawableId = R.drawable.ic_transactions_empty_state
//                )
            }
        }
    }

}

@Composable
@Preview
fun PreviewBookListMainContent() {

}