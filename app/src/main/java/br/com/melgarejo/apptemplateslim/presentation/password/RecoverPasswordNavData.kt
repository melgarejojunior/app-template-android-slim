package br.com.melgarejo.apptemplateslim.presentation.password

import android.content.Context
import android.content.Intent
import br.com.melgarejo.apptemplateslim.presentation.structure.navigation.NavData


class RecoverPasswordNavData : NavData {
    override fun createIntent(context: Context): Intent {
        return RecoverPasswordActivity.createIntent(context)
    }
}