package br.com.melgarejo.apptemplateslim.presentation

import android.support.multidex.MultiDexApplication
import br.com.melgarejo.apptemplateslim.data.storage.PreferencesCache
import br.com.melgarejo.apptemplateslim.domain.Singletons
import com.facebook.stetho.Stetho

class AppTemplateSlimApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        Singletons.cache = PreferencesCache.init(this)
        Stetho.initializeWithDefaults(this)
    }
}
