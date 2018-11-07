package br.com.melgarejo.apptemplateslim.data.remote.repository

import br.com.melgarejo.apptemplateslim.data.remote.client.ApiClient
import br.com.melgarejo.apptemplateslim.data.remote.entity.ApiUser
import br.com.melgarejo.apptemplateslim.domain.boundary.UserRepository
import br.com.melgarejo.apptemplateslim.domain.entity.User
import io.reactivex.Completable
import io.reactivex.Single


class DefaultUserRepository : UserRepository {

    override fun signIn(email: String, password: String): Single<User> {
        return ApiClient.signIn(email, password).map(ApiUser.ApiUserToUserMapper::transform)
    }

    override fun getCurrent(): Single<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signInWithFacebook(accessToken: String): Single<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signUp(): Single<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sendPasswordRecovery(email: String): Completable {
        return ApiClient.sendPasswordRecovery(email)
    }
}