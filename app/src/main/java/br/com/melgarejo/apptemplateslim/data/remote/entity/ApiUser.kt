package br.com.melgarejo.apptemplateslim.data.remote.entity

import com.google.gson.annotations.SerializedName

data class ApiUser(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("avatar") val avatar: ApiImage?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("token") val token: String?
)
