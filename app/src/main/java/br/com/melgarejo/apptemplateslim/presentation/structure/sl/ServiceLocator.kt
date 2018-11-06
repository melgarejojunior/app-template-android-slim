package br.com.melgarejo.apptemplateslim.presentation.structure.sl

import android.content.Context
import br.com.melgarejo.apptemplateslim.data.remote.mapper.ApiUserToUserMapper
import br.com.melgarejo.apptemplateslim.data.remote.mapper.Mapper
import br.com.melgarejo.apptemplateslim.data.remote.repository.DefaultUserRepository
import br.com.melgarejo.apptemplateslim.data.storage.PreferencesCache
import br.com.melgarejo.apptemplateslim.domain.boundary.UserRepository
import br.com.melgarejo.apptemplateslim.domain.boundary.resources.Cache
import br.com.melgarejo.apptemplateslim.domain.boundary.resources.Logger
import br.com.melgarejo.apptemplateslim.domain.boundary.resources.StringsProvider
import br.com.melgarejo.apptemplateslim.domain.interactor.user.GetPersistedUser
import br.com.melgarejo.apptemplateslim.domain.interactor.user.RecoverPassword
import br.com.melgarejo.apptemplateslim.domain.interactor.user.SignIn
import br.com.melgarejo.apptemplateslim.domain.interactor.user.SignInWithFacebook
import br.com.melgarejo.apptemplateslim.domain.util.SchedulerProvider
import br.com.melgarejo.apptemplateslim.presentation.landing.SplashViewModel
import br.com.melgarejo.apptemplateslim.presentation.login.LoginViewModel
import br.com.melgarejo.apptemplateslim.presentation.password.recover.RecoverPasswordViewModel
import br.com.melgarejo.apptemplateslim.presentation.util.ErrorHandler
import br.com.melgarejo.apptemplateslim.presentation.util.resources.AndroidLogger
import br.com.melgarejo.apptemplateslim.presentation.util.resources.AndroidStringProvider
import br.com.melgarejo.apptemplateslim.presentation.util.resources.DefaultSchedulerProvider
import br.com.melgarejo.apptemplateslim.presentation.util.resources.LoginAction
import kotlin.reflect.KClass

interface ServiceLocator {
    companion object {
        private var INSTANCE: ServiceLocator? = null
        fun getInstance() = INSTANCE
        fun getInstance(context: Context): ServiceLocator {
            return INSTANCE ?: DefaultServiceLocator(context).also {
                INSTANCE = it
            }
        }
    }

    val cache: Cache
    val logger: Logger
    val strings: StringsProvider
    val schedulerProvider: SchedulerProvider

    fun get(type: KClass<*>): Any

    fun <I, O> getMapper(mapperType: MapperType): Mapper<I, O>
}

open class DefaultServiceLocator(private val context: Context) : ServiceLocator {

    override val cache: Cache
        get() {
            if (singletonCache == null) singletonCache = PreferencesCache.init(context)
            return singletonCache!!
        }
    override val logger: Logger get() = AndroidLogger(context)
    override val strings: StringsProvider get() = AndroidStringProvider(context)
    override val schedulerProvider: SchedulerProvider get() = DefaultSchedulerProvider()

    private var singletonCache: Cache? = null
    private val loginAction = LoginAction(context, cache)

    override fun get(type: KClass<*>): Any {
        return when (type) {
        /* Utils */
            ErrorHandler::class -> ErrorHandler(strings, logger, loginAction)
            GetPersistedUser::class -> GetPersistedUser()
        /*Repositories*/
            UserRepository::class -> DefaultUserRepository(getMapper(MapperType.API_USER_TO_USER))
        /*Interactor*/
            SignIn::class -> SignIn(get(UserRepository::class) as UserRepository)
            SignInWithFacebook::class -> SignInWithFacebook(get(UserRepository::class) as UserRepository)
            RecoverPassword::class -> RecoverPassword(get(UserRepository::class) as UserRepository)
        /* ViewModels*/
            SplashViewModel::class -> SplashViewModel(get(GetPersistedUser::class) as GetPersistedUser)
            LoginViewModel::class ->
                LoginViewModel(
                        get(SignIn::class) as SignIn,
                        get(SignInWithFacebook::class) as SignInWithFacebook,
                        schedulerProvider,
                        strings
                )
            RecoverPasswordViewModel::class ->
                RecoverPasswordViewModel(
                        get(RecoverPassword::class) as RecoverPassword,
                        schedulerProvider,
                        strings
                )
            else -> throw InstanceNotFoundException("$type was not found by ServiceLocator.class")
        }
    }

    override fun <I, O> getMapper(mapperType: MapperType): Mapper<I, O> {
        return when (mapperType) {
            MapperType.API_USER_TO_USER -> ApiUserToUserMapper()
        } as Mapper<I, O>
    }
}