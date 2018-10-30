package br.com.melgarejo.apptemplateslim.domain.interactor.user

import br.com.melgarejo.apptemplateslim.domain.boundary.UserRepository
import br.com.melgarejo.apptemplateslim.domain.entity.User
import br.com.melgarejo.apptemplateslim.presentation.structure.sl.ServiceLocator


class GetPersistedUser {
    fun execute(): User? {
        return try {
            ServiceLocator.getInstance()?.run {
                cache.get(UserRepository.CURRENT_USER, User::class.java) as User
            }
        } catch (t: Throwable) {
            null
        }
    }
}