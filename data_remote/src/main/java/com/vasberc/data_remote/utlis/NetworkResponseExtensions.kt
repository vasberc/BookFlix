package com.vasberc.data_remote.utlis

import com.haroldadmin.cnradapter.NetworkResponse
import com.vasberc.domain.model.Domainable
import com.vasberc.domain.model.Error
import com.vasberc.domain.model.ResultState
import java.io.IOException

fun <T : Domainable<R>, U : Domainable<Error>, R : Any> NetworkResponse<T, U>.toDomain(): ResultState<R> =

    when (this) {
        is NetworkResponse.Success -> ResultState.Success(this.body.toDomain())
        else -> {
            when (this) {
                is NetworkResponse.ServerError -> ResultState.Error(this.body!!.toDomain(this.code ?: 400))
                is NetworkResponse.NetworkError -> ResultState.Error(checkError(this.error))
                is NetworkResponse.UnknownError -> ResultState.Error(checkError(this.error))
                else -> throw IllegalArgumentException("Unhandled network response type: $this")
            }
        }
    }

private fun checkError(throwable: Throwable): Error = when (throwable) {
    is IOException -> Error.Network
    else -> Error.Unknown(throwable.message)
}