package br.com.melgarejo.apptemplateslim.presentation.structure.navigation

import android.content.Context
import br.com.melgarejo.apptemplateslim.presentation.dashboard.MainActivity
import br.com.melgarejo.apptemplateslim.presentation.login.LoginActivity
import br.com.melgarejo.apptemplateslim.presentation.password.RecoverPasswordActivity
import br.com.melgarejo.apptemplateslim.presentation.signup.SignUpActivity
import br.com.melgarejo.apptemplateslim.presentation.util.extensions.shouldClearTask


object Navigator {

    fun goTo(context: Context?, navData: NavData) {
        context?.let {
            val intent = when (navData) {
//                                    TODO(Add activity call here)
                is NavData.RecoverPasswordNavData -> RecoverPasswordActivity.createIntent(it)
                is NavData.SignUpNavData -> SignUpActivity.createIntent(it)
                else -> throw RuntimeException("Don't know where should it go to. NavData ${navData.javaClass.simpleName}")
            }
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