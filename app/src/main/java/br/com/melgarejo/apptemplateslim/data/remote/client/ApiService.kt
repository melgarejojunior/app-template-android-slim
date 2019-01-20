package br.com.melgarejo.apptemplateslim.data.remote.client

import br.com.melgarejo.apptemplateslim.data.remote.entity.ApiUser
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("users/sign_in")
    fun signIn(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("token") token: String?,
        @Field("platform") platform: String
    ): Single<Response<ApiUser>>

    @FormUrlEncoded
    @POST("auth/facebook")
    fun signInWithFacebook(@Field("access_token") accessToken: String): Single<Response<ApiUser>>

    @POST("users")
    fun signUp(@Body requestBody: RequestBody): Single<Response<ApiUser>>

    @FormUrlEncoded
    @POST("users/recover_password")
    fun sendPasswordRecovery(@Field("email") email: String): Single<Response<Void>>
}
