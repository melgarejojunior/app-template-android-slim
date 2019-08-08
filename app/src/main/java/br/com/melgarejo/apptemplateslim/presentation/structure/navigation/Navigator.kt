package br.com.melgarejo.apptemplateslim.presentation.structure.navigation

import android.content.Context
import br.com.melgarejo.apptemplateslim.presentation.dashboard.MainActivity
import br.com.melgarejo.apptemplateslim.presentation.login.LoginActivity
import br.com.melgarejo.apptemplateslim.presentation.util.extensions.shouldClearTask


object Navigator {

    fun goTo(context: Context?, navData: NavData) {
        context?.let {
            val intent = navData.createIntent(it)
            it.startActivity(intent)
        }
    }

    fun goToMain(context: Context, clearTask: Boolean = false) {
        context.startActivity(MainActivity.createIntent(context).apply { shouldClearTask(clearTask) })
    }

    fun goToLogin(context: Context, clearTask: Boolean = false) {
        context.startActivity(LoginActivity.createIntent(context).apply { shouldClearTask(clearTask) })
    }

}