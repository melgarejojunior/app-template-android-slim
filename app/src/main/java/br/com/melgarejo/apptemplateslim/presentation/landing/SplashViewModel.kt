package br.com.melgarejo.apptemplateslim.presentation.landing

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.OnLifecycleEvent
import br.com.melgarejo.apptemplateslim.domain.interactor.user.GetPersistedUser
import br.com.melgarejo.apptemplateslim.presentation.structure.base.BaseViewModel
import br.com.melgarejo.apptemplateslim.presentation.util.extensions.defaultMutableLiveData

class SplashViewModel(
    private val getPersistedUser: GetPersistedUser
) : BaseViewModel() {

    val goToMain: LiveData<Boolean> get() = goToMainLiveData
    val goToLogin: LiveData<Boolean> get() = goToLoginLiveData

    private val goToMainLiveData = defaultMutableLiveData(false)
    private val goToLoginLiveData = defaultMutableLiveData(false)

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun checkUser() {
        if (getPersistedUser.execute() != null) {
            goToLoginLiveData.postValue(true)
        } else {
            goToMainLiveData.postValue(true)
        }
    }
}