package br.com.melgarejo.apptemplateslim.presentation.login

import android.content.Context
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import br.com.melgarejo.apptemplateslim.R
import br.com.melgarejo.apptemplateslim.databinding.ActivityLoginBinding
import br.com.melgarejo.apptemplateslim.domain.extensions.then
import br.com.melgarejo.apptemplateslim.presentation.structure.base.BaseActivity
import br.com.melgarejo.apptemplateslim.presentation.structure.base.BaseViewModel
import br.com.melgarejo.apptemplateslim.presentation.structure.navigation.Navigator
import br.com.melgarejo.apptemplateslim.presentation.structure.sl.ServiceLocator
import br.com.melgarejo.apptemplateslim.presentation.util.extensions.observe
import br.com.melgarejo.apptemplateslim.presentation.util.extensions.observeChanges
import br.com.melgarejo.apptemplateslim.presentation.util.extensions.setError
import br.com.melgarejo.apptemplateslim.presentation.util.extensions.setOnClickListener

class LoginActivity : BaseActivity() {


    override val sl: ServiceLocator get() = ServiceLocator.getInstance(this.applicationContext)
    override val baseViewModel: BaseViewModel get() = viewModel

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = sl.get(LoginViewModel::class.java)
        lifecycle.addObserver(viewModel)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        setupUi()
        super.onCreate(savedInstanceState)
    }

    override fun subscribeUi() {
        super.subscribeUi()
        viewModel.showEmailFieldError.observe(this, this::onNextEmailError)
        viewModel.showPasswordFieldError.observe(this, this::onNextPasswordError)
        viewModel.goToMain.observe(this, this::onNextGoToMain)
    }

    private fun setupUi() {
        binding.emailInput.observeChanges(viewModel::onEmailChanged)
        binding.passwordInput.observeChanges(viewModel::onPasswordChanged)
        binding.facebookButton.setOnClickListener(viewModel::onFacebookButtonClicked)
        binding.googleButton.setOnClickListener(viewModel::onGoogleButtonClicked)
        binding.recoverPasswordButton.setOnClickListener(viewModel::onRecoverPasswordClicked)
        binding.registerButton.setOnClickListener(viewModel::onSignUpClicked)
        binding.submitButton.setOnClickListener(viewModel::onSubmitClicked)
    }

    private fun onNextGoToMain(shouldGo: Boolean?) {
        shouldGo?.let { Navigator.goToMain(this, true) }
    }

    private fun onNextEmailError(shouldShowError: Boolean?) {
        shouldShowError?.let {
            binding.emailInput.setError(it then R.string.error_invalid_email)
        }
    }

    private fun onNextPasswordError(shouldShowError: Boolean?) {
        shouldShowError?.let {
            binding.passwordInput.setError(it then R.string.error_invalid_password)

        }
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }
}
