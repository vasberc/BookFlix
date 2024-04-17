package com.vasberc.domain.model

sealed class ErrorModel {

    sealed class NetworkError : ErrorModel() {
        data object Network : NetworkError()
        data object Timeout : NetworkError()
        data object ServerUnavailable : NetworkError()
    }

    //HTTP Error 4xx
    data class ServerError(
        val message: String,
        val status: Int,
    ) : ErrorModel()

    data class Unknown(val throwableString: String? = null) : ErrorModel()
}