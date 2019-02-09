package br.com.melgarejo.apptemplateslim.data.remote.entity

import br.com.melgarejo.apptemplateslim.data.remote.mapper.Mapper
import br.com.melgarejo.apptemplateslim.domain.entity.User
import com.google.gson.annotations.SerializedName

data class ApiUser(
        @SerializedName("id") val id: String?,
        @SerializedName("name") val name: String?,
        @SerializedName("email") val email: String?,
        @SerializedName("avatar") val avatar: ApiImage?,
        @SerializedName("phone") val phone: String?,
        @SerializedName("token") val token: String?
) {
    object ApiUserToUserMapper : Mapper<ApiUser, User>() {
        override fun transform(t: ApiUser) = User(
                id = t.id,
                name = t.name,
                phone = t.phone,
                email = t.email,
                token = t.token,
                avatarUrl = t.avatar?.medium,
                avatarThumbUrl = t.avatar?.thumb
        )
    }
}
