package br.com.melgarejo.apptemplateslim.data.remote.mapper

import br.com.melgarejo.apptemplateslim.data.remote.entity.ApiUser
import br.com.melgarejo.apptemplateslim.domain.entity.User

class ApiUserToUserMapper : Mapper<ApiUser, User> {
    override fun transform(t: ApiUser) = User(
            t.id,
            t.name,
            t.phone,
            t.email,
            t.token,
            t.avatar?.medium,
            t.avatar?.thumb
    )

    override fun transform(items: List<ApiUser>?): List<User>? {
        return items?.map(this::transform)
    }
}