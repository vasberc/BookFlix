package com.vasberc.presentation.screens.bookdetailed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vasberc.domain.model.BookDetailed
import com.vasberc.domain.model.ErrorModel
import com.vasberc.domain.usecase.GetBookDetailedDataUseCase
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class BookDetailedViewModel(
    private val getBookDetailedDataUseCase: GetBookDetailedDataUseCase,
    //Provides also the current destinations arguments
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var bookDetailedScreenState by mutableStateOf(BookDetailedScreenState())
        private set

    init {
        val bookId = savedStateHandle.get<Int>("bookId")
        if(bookId != null) {
            getBookDetailedData(bookId)
        } else {
            bookDetailedScreenState = BookDetailedScreenState(
                loading = false,
                message = "Something went wrong, please try again!"
            )
        }
    }

    private fun getBookDetailedData(bookId: Int) {
        viewModelScope.launch {
            getBookDetailedDataUseCase(bookId).collect { resultState ->
                resultState.handle(
                    onLoading = {
                        bookDetailedScreenState = BookDetailedScreenState()
                    },
                    onSuccess = {
                        bookDetailedScreenState = BookDetailedScreenState(loading = false, data = it)
                    },
                    onError = {
                        bookDetailedScreenState = BookDetailedScreenState(
                            loading = false,
                            message = if(it is ErrorModel.NetworkError) {
                                "Your connection appears to be offline, please try again later!"
                            } else {
                                "Something went wrong, please try again!"
                            }
                        )
                    }
                )
            }
        }
    }

    data class BookDetailedScreenState(
        val loading: Boolean = true,
        val message: String? = null,
        val data: BookDetailed? = null
    )
}