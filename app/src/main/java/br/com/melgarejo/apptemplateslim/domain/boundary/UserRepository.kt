package br.com.melgarejo.apptemplateslim.domain.boundary

import br.com.melgarejo.apptemplateslim.domain.entity.User
import br.com.melgarejo.apptemplateslim.domain.interactor.user.SignUp
import io.reactivex.Completable
import io.reactivex.Single

interface UserRepository {
    companion object {
        const val CURRENT_USER = "CURRENT_USER"
    }

    fun getCurrentFromRemote(): Single<User>
    fun signIn(email: String, password: String, token: String?): Single<User>
    fun signInWithFacebook(): Single<User>
    fun signUp(user: SignUp.Fields): Single<User>
    fun sendPasswordRecovery(email: String): Completable
    fun cacheUser(user: User)
}