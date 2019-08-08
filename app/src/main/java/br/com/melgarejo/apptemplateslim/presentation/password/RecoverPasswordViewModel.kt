package br.com.melgarejo.apptemplateslim.presentation.password

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import br.com.melgarejo.apptemplateslim.domain.boundary.resources.SchedulerProvider
import br.com.melgarejo.apptemplateslim.domain.boundary.resources.StringsProvider
import br.com.melgarejo.apptemplateslim.domain.extensions.defaultPlaceholders
import br.com.melgarejo.apptemplateslim.domain.extensions.defaultSched
import br.com.melgarejo.apptemplateslim.domain.interactor.user.InvalidFieldsException
import br.com.melgarejo.apptemplateslim.domain.interactor.user.RecoverPassword
import br.com.melgarejo.apptemplateslim.presentation.structure.arch.Event
import br.com.melgarejo.apptemplateslim.presentation.structure.base.BaseViewModel
import br.com.melgarejo.apptemplateslim.presentation.util.viewmodels.DialogData
import io.reactivex.disposables.Disposable


class RecoverPasswordViewModel(
        private val recoverPassword: RecoverPassword,
        private val schedulerProvider: SchedulerProvider,
        private val stringsProvider: StringsProvider
) : BaseViewModel() {


    val showEmailFieldError: LiveData<Event<Boolean>> get() = showEmailFieldErrorLiveData
    val close: LiveData<Event<Boolean>> get() = closeLiveData

    private val showEmailFieldErrorLiveData = MutableLiveData<Event<Boolean>>()
    private val closeLiveData = MutableLiveData<Event<Boolean>>()


    private var sendRecoveryDisposable: Disposable? = null
    private var email: String? = null

    fun onSubmitButtonClick() {
        if (sendRecoveryDisposable == null) startSendPasswordRecovery()
    }

    fun onEmailChanged(email: String) {
        this.email = email
    }

    private fun startSendPasswordRecovery() {
        email?.let {
            sendRecoveryDisposable = recoverPassword.execute(it)
                    .defaultPlaceholders(this::setPlaceholder)
                    .defaultSched(schedulerProvider)
                    .subscribe({ this.handleSendRecoverySuccess() }, ::handleSendRecoveryFailure)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun disposePasswordRecovery() {
        if (sendRecoveryDisposable != null) {
            sendRecoveryDisposable!!.dispose()
            sendRecoveryDisposable = null
        }
    }

    private fun handleSendRecoverySuccess() {
        super.setDialog(
                DialogData.message(
                        stringsProvider.activityRecoverPassword,
                        stringsProvider.activityRecoverPasswordSuccess,
                        ::sendCloseSignal,
                        null,
                        false
                )
        )
    }

    private fun sendCloseSignal() {
        closeLiveData.postValue(Event(true))
    }

    private fun handleSendRecoveryFailure(throwable: Throwable) {
        if (throwable is InvalidFieldsException) {
            if (throwable.getFields().contains(InvalidFieldsException.EMAIL)) {
                showEmailFieldErrorLiveData.postValue(Event(true))
            }
        } else {
            super.setDialog(throwable, ::startSendPasswordRecovery, null)
        }
    }
}