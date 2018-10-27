package br.com.melgarejo.apptemplateslim.presentation.landing

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import br.com.melgarejo.apptemplateslim.domain.interactor.user.GetPersistedUser
import br.com.melgarejo.apptemplateslim.presentation.structure.base.BaseViewModel

class SplashViewModel(
        private val getPersistedUser: GetPersistedUser
) : BaseViewModel() {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun checkUser() {
        if (getPersistedUser.execute() != null) {

        }
    }
}