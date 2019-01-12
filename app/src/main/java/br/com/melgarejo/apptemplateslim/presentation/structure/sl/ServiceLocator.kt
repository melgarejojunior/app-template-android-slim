package br.com.melgarejo.apptemplateslim.presentation.structure.sl

import android.content.Context
import br.com.melgarejo.apptemplateslim.data.remote.repository.DefaultUserRepository
import br.com.melgarejo.apptemplateslim.data.storage.PreferencesCache
import br.com.melgarejo.apptemplateslim.domain.boundary.UserRepository
import br.com.melgarejo.apptemplateslim.domain.boundary.resources.Cache
import br.com.melgarejo.apptemplateslim.domain.boundary.resources.Logger
import br.com.melgarejo.apptemplateslim.domain.boundary.resources.StringsProvider
import br.com.melgarejo.apptemplateslim.domain.interactor.user.GetPersistedUser
import br.com.melgarejo.apptemplateslim.domain.interactor.user.SignIn
import br.com.melgarejo.apptemplateslim.presentation.landing.SplashViewModel
import br.com.melgarejo.apptemplateslim.presentation.util.ErrorHandler
import br.com.melgarejo.apptemplateslim.presentation.util.resources.AndroidLogger
import br.com.melgarejo.apptemplateslim.presentation.util.resources.AndroidStringProvider
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

    fun get(type: KClass<*>): Any
}

open class DefaultServiceLocator(private val context: Context) : ServiceLocator {

    override val cache: Cache
        get() {
            if (singletonCache == null) singletonCache = PreferencesCache.init(context)
            return singletonCache!!
        }
    override val logger: Logger get() = AndroidLogger(context)
    override val strings: StringsProvider get() = AndroidStringProvider(context)

    private var singletonCache: Cache? = null
    private val loginAction = LoginAction(context, cache)

    override fun get(type: KClass<*>): Any {
        return when (type) {
            /* Utils */
            ErrorHandler::class -> ErrorHandler(strings, logger, loginAction)
            StringsProvider::class -> AndroidStringProvider(context)
            /*Repositories*/
            UserRepository::class -> DefaultUserRepository()
            /*Interactors*/
            GetPersistedUser::class -> GetPersistedUser()
            SignIn::class -> SignIn(get(UserRepository::class) as UserRepository)
            /* ViewModels*/
            SplashViewModel::class -> SplashViewModel(get(GetPersistedUser::class) as GetPersistedUser)
            else -> throw InstanceNotFoundException("$type was not found")
        }
    }
}