package br.com.melgarejo.apptemplateslim.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.melgarejo.apptemplateslim.domain.boundary.resources.SchedulerProvider
import br.com.melgarejo.apptemplateslim.domain.extensions.defaultSched
import br.com.melgarejo.apptemplateslim.domain.interactor.user.InvalidFieldsException
import br.com.melgarejo.apptemplateslim.domain.interactor.user.LoginForm
import br.com.melgarejo.apptemplateslim.domain.interactor.user.SignIn
import br.com.melgarejo.apptemplateslim.presentation.password.RecoverPasswordNavData
import br.com.melgarejo.apptemplateslim.presentation.signup.SignUpNavData
import br.com.melgarejo.apptemplateslim.presentation.structure.base.BaseViewModel
import br.com.melgarejo.apptemplateslim.presentation.structure.navigation.NavData
import br.com.melgarejo.apptemplateslim.presentation.util.extensions.defaultPlaceholders
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy


class LoginViewModel(
        private val signIn: SignIn,
        private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    val showEmailFieldError: LiveData<Boolean> get() = showEmailFieldErrorLiveData
    val showPasswordFieldError: LiveData<Boolean> get() = showPasswordFieldErrorLiveData
    val goToMain: LiveData<Boolean> get() = goToMainLiveData

    private val showEmailFieldErrorLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val showPasswordFieldErrorLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val goToMainLiveData: MutableLiveData<Boolean> = MutableLiveData()

    private var form = LoginForm()
    private var signInDisposable: Disposable? = null


    fun onEmailChanged(email: String) {
        form.email = email
    }

    fun onPasswordChanged(password: String) {
        form.password = password
    }

    fun onFacebookButtonClicked() {}

    fun onGoogleButtonClicked() {}

    fun onRecoverPasswordClicked() {
        goTo(RecoverPasswordNavData())
    }

    fun onSignUpClicked() {
        goTo(SignUpNavData())
    }

    fun onSubmitClicked() {
        form.useForm(this::submit)?.let { showFieldErrors(it) }
    }

    private fun submit(email: String, password: String) {
        signInDisposable?.dispose()
        signInDisposable = signIn.default(email, password, null)
                .defaultPlaceholders(this::setPlaceholder)
                .defaultSched(schedulerProvider)
                .subscribeBy(this::onFailure) {
                    showEmailFieldErrorLiveData.value = false
                    showPasswordFieldErrorLiveData.value = false
                    goToMainLiveData.value = true
                }
    }

    private fun onFailure(throwable: Throwable) {
        if (throwable is InvalidFieldsException) {
            showFieldErrors(throwable)
        } else {
            setDialog(throwable, this::onSubmitClicked)
        }
    }

    private fun showFieldErrors(e: InvalidFieldsException) {
        for (field in e.getFields()) {
            showFieldError(field)
        }
    }

    private fun showFieldError(field: Int) {
        when (field) {
            InvalidFieldsException.EMAIL -> showEmailFieldErrorLiveData.value = true
            InvalidFieldsException.PASSWORD -> showPasswordFieldErrorLiveData.value = true
        }
    }


}