@file:Suppress("UNCHECKED_CAST")

package br.com.melgarejo.apptemplateslim.presentation.structure.navigation

import br.com.melgarejo.apptemplateslim.data.remote.repository.DefaultUserRepository
import br.com.melgarejo.apptemplateslim.domain.boundary.UserRepository
import br.com.melgarejo.apptemplateslim.domain.boundary.resources.SchedulerProvider
import br.com.melgarejo.apptemplateslim.domain.boundary.resources.StringsProvider
import br.com.melgarejo.apptemplateslim.domain.interactor.user.GetPersistedUser
import br.com.melgarejo.apptemplateslim.domain.interactor.user.RecoverPassword
import br.com.melgarejo.apptemplateslim.domain.interactor.user.SignIn
import br.com.melgarejo.apptemplateslim.domain.interactor.user.SignUp
import br.com.melgarejo.apptemplateslim.presentation.landing.SplashViewModel
import br.com.melgarejo.apptemplateslim.presentation.login.LoginViewModel
import br.com.melgarejo.apptemplateslim.presentation.password.RecoverPasswordViewModel
import br.com.melgarejo.apptemplateslim.presentation.signup.SignUpViewModel
import br.com.melgarejo.apptemplateslim.presentation.structure.sl.InstanceNotFoundException
import br.com.melgarejo.apptemplateslim.presentation.structure.sl.ServiceLocator
import br.com.melgarejo.apptemplateslim.presentation.util.ErrorHandler
import br.com.melgarejo.apptemplateslim.presentation.util.resources.AndroidStringProvider
import kotlin.reflect.KClass

object InstanceMaker {

    private val serviceLocator = ServiceLocator.getInstance()

    inline fun <reified T : Any> get(): T {
        return `access$getByType`(type = T::class)
    }

    @PublishedApi
    internal fun <T : Any> `access$getByType`(type: KClass<T>) = getByType(type)

    private fun <T : Any> getByType(type: KClass<T>): T {
        return when (type) {
            /***
             * Utils
             ***/
            ErrorHandler::class -> ErrorHandler(serviceLocator.strings, serviceLocator.logger, serviceLocator.loginAction) as T
            StringsProvider::class -> AndroidStringProvider(serviceLocator.context) as T
            SchedulerProvider::class -> serviceLocator.schedulerProvider as T
            /***
             * Repositories
             ***/
            UserRepository::class -> DefaultUserRepository(serviceLocator.cache) as T
            /***
             * Interactors
             ***/
            GetPersistedUser::class -> GetPersistedUser() as T
            SignIn::class -> SignIn(getByType(UserRepository::class)) as T
            SignUp::class -> SignUp(getByType(UserRepository::class)) as T
            RecoverPassword::class -> RecoverPassword(getByType(UserRepository::class)) as T
            /***
             * ViewModels
             ***/
            SplashViewModel::class -> SplashViewModel(getByType(GetPersistedUser::class)) as T
            LoginViewModel::class -> LoginViewModel(getByType(SignIn::class), serviceLocator.schedulerProvider) as T
            RecoverPasswordViewModel::class -> RecoverPasswordViewModel(
                getByType(RecoverPassword::class),
                serviceLocator.schedulerProvider,
                serviceLocator.strings
            ) as T
            SignUpViewModel::class -> SignUpViewModel(
                getByType(SignUp::class),
                serviceLocator.schedulerProvider
            ) as T
            else -> throw InstanceNotFoundException("${type::class.simpleName} was not found")
        }
    }
}