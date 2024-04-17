package com.vasberc.domain.model

sealed class ResultState <out T: Any> {
    data class Success<T: Any>(val data: T): ResultState<T>()
    data class Error(val error: ErrorModel): ResultState<Nothing>()

    data object Loading: ResultState<Nothing>()

    fun handle(
        onLoading: (() -> Unit)? = null,
        onSuccess: ((T) -> Unit)? = null,
        onError: ((ErrorModel) -> Unit)? = null
    ) {
        when(this) {
            is Loading -> onLoading?.invoke()
            is Error -> onError?.invoke(this.error)
            is Success -> onSuccess?.invoke(this.data)
        }
    }
}