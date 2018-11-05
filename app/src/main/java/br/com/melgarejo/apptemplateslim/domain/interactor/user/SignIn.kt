package br.com.melgarejo.apptemplateslim.domain.interactor.user

import br.com.melgarejo.apptemplateslim.domain.boundary.UserRepository
import br.com.melgarejo.apptemplateslim.domain.entity.User
import io.reactivex.Single

class SignIn constructor(private val userRepository: UserRepository) {

    fun execute(email: String, password: String): Single<User> {
        return Single.just(FormFields().withEmail(email).withPassword(password))
                .doOnSuccess { formFields -> if (!formFields.isValid) throw formFields.exception!! }
                .flatMap { _ -> userRepository.signIn(email, password) }
    }
}
