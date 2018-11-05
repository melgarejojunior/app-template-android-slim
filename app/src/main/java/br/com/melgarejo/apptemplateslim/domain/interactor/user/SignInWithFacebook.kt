package br.com.melgarejo.apptemplateslim.domain.interactor.user

import br.com.melgarejo.apptemplateslim.domain.boundary.UserRepository
import br.com.melgarejo.apptemplateslim.domain.entity.User
import io.reactivex.Single

class SignInWithFacebook constructor(private val userRepository: UserRepository) {

    fun execute(accessToken: String): Single<User> {
        return userRepository.signInWithFacebook(accessToken)
    }
}