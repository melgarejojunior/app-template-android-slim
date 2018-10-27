package br.com.melgarejo.apptemplateslim.presentation.structure.sl

import android.content.Context
import br.com.melgarejo.apptemplateslim.data.storage.PreferencesCache
import br.com.melgarejo.apptemplateslim.domain.Cache
import java.lang.reflect.Type

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

    fun <T> get(type: Type): T
}

open class DefaultServiceLocator(private val context: Context) : ServiceLocator {

    private val instances: MutableMap<Type, Any> = mutableMapOf()

    override val cache: Cache by lazy {
        PreferencesCache.init(context)
    }


    override fun <T> get(type: Type): T {
        return if (instances.containsKey(type)) {
            instances[type] as T
        } else {
            throw InstanceNotFoundException("$type was not found")
        }
    }
}