package br.com.melgarejo.apptemplateslim.presentation.landing

import android.os.Bundle
import br.com.melgarejo.apptemplateslim.presentation.structure.base.BaseActivity
import br.com.melgarejo.apptemplateslim.presentation.structure.base.BaseViewModel
import br.com.melgarejo.apptemplateslim.presentation.structure.navigation.Navigator
import br.com.melgarejo.apptemplateslim.presentation.structure.sl.ServiceLocator
import br.com.melgarejo.apptemplateslim.presentation.util.extensions.observe

class SplashActivity : BaseActivity() {

    override val sl: ServiceLocator get() = ServiceLocator.getInstance(this.applicationContext)
    override val baseViewModel: BaseViewModel get() = viewModel

    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = sl.get(SplashViewModel::class.java)
        lifecycle.addObserver(viewModel)
        super.onCreate(savedInstanceState)
    }

    override fun subscribeUi() {
        super.subscribeUi()
        viewModel.goToMain.observe(this, ::onNextGoToMainValue)
        viewModel.goToLogin.observe(this, ::onNextGoToLoginValue)
    }

    private fun onNextGoToMainValue(shouldGo: Boolean?) {
        shouldGo?.let { if (it) Navigator.goToMain(this, true) }
    }

    private fun onNextGoToLoginValue(shouldGo: Boolean?) {
        shouldGo?.let { if (it) Navigator.goToLogin(this, true) }
    }
}