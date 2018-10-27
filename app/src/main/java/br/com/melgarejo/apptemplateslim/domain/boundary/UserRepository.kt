package br.com.melgarejo.apptemplateslim.domain.boundary

import br.com.melgarejo.apptemplateslim.domain.entity.User
import io.reactivex.Completable
import io.reactivex.Single


interface UserRepository {
    companion object {
        const val CURRENT_USER = "CURRENT_USER"
    }

    fun getCurrent(): Single<User>
    fun signIn(): Single<User>
    fun signInWithFacebook(): Single<User>
    fun signUp(): Single<User>
    fun sendPasswordRecovery(email: String): Completable
}