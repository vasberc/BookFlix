package com.vasberc.presentation.screens.booklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vasberc.domain.usecase.GetBooksUseCase
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class BookListViewModel(
    getBooksUseCase: GetBooksUseCase
): ViewModel() {

    val pagerFlow by lazy {
        getBooksUseCase(viewModelScope)
    }
}