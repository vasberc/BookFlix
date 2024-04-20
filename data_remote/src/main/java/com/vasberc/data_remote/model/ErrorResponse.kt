package com.vasberc.data_remote.model


import com.google.gson.annotations.SerializedName
import com.vasberc.domain.model.Domainable
import com.vasberc.domain.model.ErrorModel

data class ErrorResponse(
    @SerializedName("detail")
    val detail: String?
): Domainable<ErrorModel> {
    override fun asDomain(vararg args: Any): ErrorModel.ServerError {
        return ErrorModel.ServerError(detail ?: "", args[0] as Int)
    }

}