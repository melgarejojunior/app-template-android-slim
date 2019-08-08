package br.com.melgarejo.apptemplateslim.presentation

import androidx.multidex.MultiDexApplication
import br.com.melgarejo.apptemplateslim.presentation.structure.sl.ServiceLocator
import com.facebook.stetho.Stetho

class AppTemplateSlimApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        ServiceLocator.getInstance(this)
        Stetho.initializeWithDefaults(this)
    }
}
