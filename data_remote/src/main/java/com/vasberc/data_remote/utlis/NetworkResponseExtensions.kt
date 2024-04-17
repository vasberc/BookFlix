package com.vasberc.data_remote.utlis

import com.haroldadmin.cnradapter.NetworkResponse
import com.vasberc.domain.model.Domainable
import com.vasberc.domain.model.ErrorModel
import com.vasberc.domain.model.ResultState
import java.io.IOException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun <T : Domainable<R>, U : Domainable<ErrorModel>, R : Any> NetworkResponse<T, U>.mapToDomain(): ResultState<R> =

    when (this) {
        is NetworkResponse.Success -> ResultState.Success(this.body.asDomain())
        else -> {
            when (this) {
                is NetworkResponse.ServerError -> ResultState.Error(this.body!!.asDomain(this.code ?: 400))
                is NetworkResponse.NetworkError -> ResultState.Error(getError(this.error))
                is NetworkResponse.UnknownError -> ResultState.Error(getError(this.error))
                else -> throw IllegalArgumentException("Unhandled network response type: $this")
            }
        }
    }

private fun getError(throwable: Throwable): ErrorModel = when (throwable) {
    is ConnectException -> ErrorModel.NetworkError.Network
    is SocketTimeoutException -> ErrorModel.NetworkError.Timeout
    is UnknownHostException -> ErrorModel.NetworkError.ServerUnavailable
    is InterruptedIOException -> ErrorModel.NetworkError.Network
    is IOException -> ErrorModel.NetworkError.Network
    else -> ErrorModel.Unknown(throwable.message)
}