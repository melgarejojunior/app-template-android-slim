package br.com.melgarejo.apptemplateslim.presentation.signup

import android.content.Context
import android.content.Intent
import br.com.melgarejo.apptemplateslim.presentation.structure.navigation.NavData


class SignUpNavData : NavData {
    override fun createIntent(context: Context): Intent {
        return SignUpActivity.createIntent(context)
    }
}