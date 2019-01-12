package br.com.melgarejo.apptemplateslim.presentation.login

import br.com.melgarejo.apptemplateslim.domain.interactor.user.SignIn
import br.com.melgarejo.apptemplateslim.presentation.structure.base.BaseViewModel

class LoginViewModel(
    private val signIn: SignIn
) : BaseViewModel() {
}