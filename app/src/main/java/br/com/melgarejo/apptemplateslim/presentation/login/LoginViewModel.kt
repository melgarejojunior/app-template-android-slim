package br.com.melgarejo.apptemplateslim.presentation.login

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import br.com.melgarejo.apptemplateslim.data.remote.client.RequestException
import br.com.melgarejo.apptemplateslim.domain.boundary.resources.StringsProvider
import br.com.melgarejo.apptemplateslim.domain.entity.User
import br.com.melgarejo.apptemplateslim.domain.interactor.user.InvalidFieldsException
import br.com.melgarejo.apptemplateslim.domain.interactor.user.SignIn
import br.com.melgarejo.apptemplateslim.domain.interactor.user.SignInWithFacebook
import br.com.melgarejo.apptemplateslim.domain.util.SchedulerProvider
import br.com.melgarejo.apptemplateslim.presentation.structure.arch.Event
import br.com.melgarejo.apptemplateslim.presentation.structure.base.BaseViewModel
import br.com.melgarejo.apptemplateslim.presentation.util.ErrorHandler
import br.com.melgarejo.apptemplateslim.presentation.util.viewmodels.DialogData
import io.reactivex.disposables.Disposable


class LoginViewModel constructor(
        private val signIn: SignIn,
        private val signInWithFacebook: SignInWithFacebook,
        private val schedulerProvider: SchedulerProvider,
        private val strings: StringsProvider,
        private val errorHandler: ErrorHandler
) : BaseViewModel() {

    val showEmailFieldError: LiveData<Event<Boolean>> get() = showEmailFieldErrorLiveData
    val showPasswordFieldError: LiveData<Event<Boolean>> get() = showPasswordFieldErrorLiveData
    val showMain: LiveData<Event<Boolean>> get() = showMainLiveData
    val showProgressIndicator: LiveData<Event<Boolean>> get() = showProgressIndicatorLiveData
    val startFacebookLogin: LiveData<Event<Boolean>> get() = startFacebookLoginLiveData

    private val showEmailFieldErrorLiveData = MutableLiveData<Event<Boolean>>()
    private val showPasswordFieldErrorLiveData = MutableLiveData<Event<Boolean>>()
    private val showMainLiveData = MutableLiveData<Event<Boolean>>()
    private val showProgressIndicatorLiveData = MutableLiveData<Event<Boolean>>()
    private val startFacebookLoginLiveData = MutableLiveData<Event<Boolean>>()

    private var signInDisposable: Disposable? = null
    private var email = ""
    private var password = ""

    fun onSubmitButtonClick() {
        if (signInDisposable == null) startSignIn()
    }

    fun onRegisterButtonClick() {
        if (signInDisposable == null) {
//            super.goTo(RegisterNavData())
        } else {
            showWaitForResultToast()
        }
    }

    fun onFacebookButtonClick() {
        startFacebookLoginLiveData.postValue(Event(true))
    }

    fun onRecoverPasswordButtonClick() {
        if (signInDisposable == null) {
//            super.goTo(RecoverPasswordNavData)
        } else {
            showWaitForResultToast()
        }
    }

    private fun showWaitForResultToast() {
        super.setToast(strings.waitForResult)
    }

    fun onShowFacebookSuccess(facebookResult: FacebookResult) {
        facebookResult.accessToken?.let {
            if (facebookResult.deniedPermissions.isEmpty()) {
                if (signInDisposable == null) {
                    startSignInWithFacebook(facebookResult.accessToken)
                } else {
                    showWaitForResultToast()
                }
            } else {
                setDialog(DialogData.error(strings, strings.errorFacebookDeniedPermissions, null, null, null))
            }
        }
    }

    fun onShowFacebookError() {
        setDialog(DialogData.error(strings, strings.errorFacebookSdk, null, null, null))
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onViewDestroyed() {
        disposeSignIn()
    }

    private fun startSignIn() {
        showProgressIndicatorLiveData.postValue(Event(true))
        signInDisposable = signIn.execute(email, password)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.main())
                .subscribe({ this.handleSignInSuccess(it) }) { throwable ->
                    handleSignInFailure(throwable) {
                        startSignIn()
                        Unit
                    }
                }

    }

    private fun startSignInWithFacebook(accessToken: String) {
        showProgressIndicatorLiveData.postValue(Event(true))
        signInDisposable = signInWithFacebook.execute(accessToken)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.main())
                .subscribe(::handleSignInSuccess) {
                    handleSignInFailure(it) { startSignInWithFacebook(accessToken) }
                }
    }


    private fun hideProgressAndRemoveDisposable() {
        showProgressIndicatorLiveData.postValue(Event(false))
        disposeSignIn()
    }

    private fun disposeSignIn() {
        if (signInDisposable != null) {
            signInDisposable!!.dispose()
            signInDisposable = null
        }
    }

    private fun handleSignInSuccess(user: User) {
        hideProgressAndRemoveDisposable()
        showMainLiveData.postValue(Event(true))
    }

    private fun handleSignInFailure(throwable: Throwable, retryAction: () -> Unit) {
        hideProgressAndRemoveDisposable()
        if (throwable is InvalidFieldsException) {
            showFieldErrors(throwable)
        } else if (throwable is RequestException) {
            if (throwable.isUnauthorizedError())
                super.setDialog(DialogData.error(strings, throwable.message
                        ?: strings.errorUnauthorizedLoginNow, strings.globalOk, null, null, null))
            else {
                super.setDialog(DialogData.error(strings, throwable.errorMessage
                        ?: throwable.message ?: strings.errorUnknown, null, null, null, null))
            }
        } else {
            super.setDialog(errorHandler.getDialogData(throwable, retryAction, null))
        }
    }

    private fun showFieldErrors(e: InvalidFieldsException) {
        for (field in e.fields) {
            showFieldError(field)
        }
    }

    private fun showFieldError(field: Int) {
        when (field) {
            InvalidFieldsException.EMAIL -> showEmailFieldErrorLiveData.postValue(Event(true))
            InvalidFieldsException.PASSWORD -> showPasswordFieldErrorLiveData.postValue(Event(true))
        }
    }

    fun onEmailChanged(email: String) {
        this.email = email
    }

    fun onPasswordChanged(password: String) {
        this.password = password
    }
}
