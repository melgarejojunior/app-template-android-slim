package br.com.melgarejo.apptemplateslim.presentation.password.recover

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import br.com.melgarejo.apptemplateslim.domain.boundary.resources.StringsProvider
import br.com.melgarejo.apptemplateslim.domain.interactor.user.InvalidFieldsException
import br.com.melgarejo.apptemplateslim.domain.interactor.user.RecoverPassword
import br.com.melgarejo.apptemplateslim.domain.util.SchedulerProvider
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
    val showProgressIndicator: LiveData<Event<Boolean>> get() = showProgressIndicatorLiveData
    val close: LiveData<Event<Boolean>> get() = closeLiveData

    private val showEmailFieldErrorLiveData = MutableLiveData<Event<Boolean>>()
    private val showProgressIndicatorLiveData = MutableLiveData<Event<Boolean>>()
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
        showProgressIndicatorLiveData.postValue(Event(true))
        email?.let {
            sendRecoveryDisposable = recoverPassword.execute(it)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.main())
                    .subscribe({ this.handleSendRecoverySuccess() }, ::handleSendRecoveryFailure)
        }
    }

    private fun hideProgressAndRemoveDisposable() {
        showProgressIndicatorLiveData.postValue(Event(false))
        disposePasswordRecovery()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun disposePasswordRecovery() {
        if (sendRecoveryDisposable != null) {
            sendRecoveryDisposable!!.dispose()
            sendRecoveryDisposable = null
        }
    }

    private fun handleSendRecoverySuccess() {
        hideProgressAndRemoveDisposable()
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
        hideProgressAndRemoveDisposable()
        if (throwable is InvalidFieldsException) {
            if (throwable.fields.contains(InvalidFieldsException.EMAIL)) {
                showEmailFieldErrorLiveData.postValue(Event(true))
            }
        } else {
            super.setDialog(throwable, ::startSendPasswordRecovery, null)
        }
    }
}