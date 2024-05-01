package com.vasberc.domain.model

sealed class Error {

    data object Network : Error()

    data class Server(
        val message: String,
        val status: Int,
    ) : Error()

    data class Unknown(val error: String? = null) : Error()
}