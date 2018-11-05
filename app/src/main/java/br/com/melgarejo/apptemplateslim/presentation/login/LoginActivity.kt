package br.com.melgarejo.apptemplateslim.presentation.login

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import br.com.melgarejo.apptemplateslim.R
import br.com.melgarejo.apptemplateslim.databinding.ActivityLoginBinding
import br.com.melgarejo.apptemplateslim.domain.boundary.resources.Logger
import br.com.melgarejo.apptemplateslim.presentation.structure.base.BaseActivity
import br.com.melgarejo.apptemplateslim.presentation.structure.base.BaseViewModel
import br.com.melgarejo.apptemplateslim.presentation.structure.navigation.Navigator
import br.com.melgarejo.apptemplateslim.presentation.structure.sl.ServiceLocator
import br.com.melgarejo.apptemplateslim.presentation.util.ViewUtils
import br.com.melgarejo.apptemplateslim.presentation.util.extensions.observeChanges
import br.com.melgarejo.apptemplateslim.presentation.util.extensions.observeEvent
import br.com.melgarejo.apptemplateslim.presentation.util.extensions.showDialog
import br.com.melgarejo.apptemplateslim.presentation.util.viewmodels.DialogData
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import java.util.*


class LoginActivity : BaseActivity() {

    override val baseViewModel: BaseViewModel get() = viewModel

    private lateinit var sl: ServiceLocator
    private lateinit var binding: ActivityLoginBinding
    private lateinit var logger: Logger
    private lateinit var viewModel: LoginViewModel
    private var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        sl = ServiceLocator.getInstance(this.applicationContext)
        logger = sl.logger
        viewModel = sl.get(LoginViewModel::class) as LoginViewModel
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.submitButton.setOnClickListener { viewModel.onSubmitButtonClick() }
        binding.registerButton.setOnClickListener { viewModel.onRegisterButtonClick() }
        binding.recoverPasswordButton.setOnClickListener { viewModel.onRecoverPasswordButtonClick() }
        binding.facebookButton.setOnClickListener { viewModel.onFacebookButtonClick() }
        subscribeTextViews()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        ensureFacebookInitialized()
        callbackManager?.let {
            if (!it.onActivityResult(requestCode, resultCode, data)) {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    override fun subscribeUi() {
        super.subscribeUi()
        viewModel.showEmailFieldError.observeEvent(this) { showEmailFieldError() }
        viewModel.showPasswordFieldError.observeEvent(this) { showPasswordFieldError() }
        viewModel.showMain.observeEvent(this, ::onNextShowMain)
        viewModel.showProgressIndicator.observeEvent(this, ::onNextProgressIndicator)
        viewModel.startFacebookLogin.observeEvent(this, ::onNextStartFacebookLogin)
    }

    private fun onNextStartFacebookLogin(shouldStart: Boolean?) {
        shouldStart?.let { if (it) showFacebookSignIn() }
    }

    private fun onNextProgressIndicator(shouldShow: Boolean?) {
        shouldShow?.let { }
    }

    private fun onNextShowMain(shouldShow: Boolean?) {
        shouldShow?.let { if (it) Navigator.goToMain(this, true) }
    }

    private fun subscribeTextViews() {
        binding.emailInput.observeChanges()?.subscribe { viewModel.onEmailChanged(it) }
        binding.passwordInput.observeChanges()?.subscribe { viewModel.onPasswordChanged(it) }
    }

    private fun showEmailFieldError() {
        ViewUtils.setTextInputLayoutError(binding.emailInput, R.string.validation_email)
    }

    private fun showPasswordFieldError() {
        ViewUtils.setTextInputLayoutError(binding.passwordInput, R.string.validation_password)
    }

    private fun showFacebookSignIn() {
        showDialog(DialogData("Not implemented", "Search for LOGIN WITH FACEBOOK ANDROID and FOLLOW the steps"))
//        TODO Uncomment this when implementing facebook login
//        ensureFacebookInitialized()
//        LoginManager.getInstance().logInWithReadPermissions(this, FACEBOOK_PERMISSIONS)
    }

    private fun ensureFacebookInitialized() {
        if (callbackManager != null) return
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                var accessToken: String? = null
                if (loginResult.accessToken != null) {
                    accessToken = loginResult.accessToken.token
                }
                viewModel.onShowFacebookSuccess(FacebookResult(
                        accessToken, loginResult.recentlyDeniedPermissions
                ))
            }

            override fun onCancel() {
                /**/
            }

            override fun onError(error: FacebookException) {
                logger.w(error)
                viewModel.onShowFacebookError()
            }
        })
    }

    companion object {
        private val FACEBOOK_PERMISSIONS = Arrays.asList("public_profile", "email")

        fun createIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

}
