package br.com.melgarejo.apptemplateslim.data.remote.client

import br.com.melgarejo.apptemplateslim.data.remote.entity.ApiError
import com.google.gson.Gson
import okhttp3.ResponseBody

object ApiErrorsFormatter {
    fun deserialize(responseBody: ResponseBody?): ApiError? {
        return Gson()
            .fromJson(responseBody?.string(), ApiError::class.java)
    }
}
