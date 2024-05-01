package com.vasberc.data_remote.model


import com.google.gson.annotations.SerializedName
import com.vasberc.domain.model.Domainable
import com.vasberc.domain.model.Error

data class ErrorResponse(
    @SerializedName("detail")
    val detail: String?
): Domainable<Error> {
    override fun toDomain(vararg args: Any): com.vasberc.domain.model.ErrorModel.Error.ServerError {
        return Error.Server(detail ?: "", args[0] as Int)
    }

}