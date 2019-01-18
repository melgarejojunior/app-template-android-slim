package br.com.melgarejo.apptemplateslim.presentation.signup

import br.com.melgarejo.apptemplateslim.domain.boundary.resources.SchedulerProvider
import br.com.melgarejo.apptemplateslim.domain.interactor.user.SignUp
import br.com.melgarejo.apptemplateslim.presentation.structure.base.BaseViewModel

class SignUpViewModel(
    private val signUp: SignUp,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {
    fun onNameChanged(name: String) {}
    fun onEmailChanged(email: String) {}
    fun onCpfChanged(cpf: String) {}
    fun onPhoneChanged(phone: String) {}
    fun onPasswordChanged(password: String) {}
    fun onPasswordConfirmationChanged(passwordConfirmation: String) {}
    fun onSubmitClicked() {}
    fun onAvatarClicked() {}
}