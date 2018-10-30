package br.com.melgarejo.apptemplateslim.data.remote.repository

import br.com.melgarejo.apptemplateslim.domain.boundary.UserRepository
import br.com.melgarejo.apptemplateslim.domain.entity.User
import io.reactivex.Completable
import io.reactivex.Single


class DefaultUserRepository() : UserRepository {
    override fun getCurrent(): Single<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signIn(): Single<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signInWithFacebook(): Single<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signUp(): Single<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sendPasswordRecovery(email: String): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}