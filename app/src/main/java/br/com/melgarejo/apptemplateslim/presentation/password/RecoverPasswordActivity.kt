package br.com.melgarejo.apptemplateslim.presentation.password

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import br.com.melgarejo.apptemplateslim.R
import br.com.melgarejo.apptemplateslim.databinding.ActivityRecoverPasswordBinding
import br.com.melgarejo.apptemplateslim.presentation.structure.base.BaseActivity
import br.com.melgarejo.apptemplateslim.presentation.structure.base.BaseViewModel
import br.com.melgarejo.apptemplateslim.presentation.structure.sl.ServiceLocator
import br.com.melgarejo.apptemplateslim.presentation.util.extensions.*


class RecoverPasswordActivity : BaseActivity() {
    override val sl: ServiceLocator get() = ServiceLocator.getInstance(this.applicationContext)
    override val baseViewModel: BaseViewModel get() = viewModel


    private lateinit var binding: ActivityRecoverPasswordBinding
    private lateinit var viewModel: RecoverPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = sl.get(RecoverPasswordViewModel::class) as RecoverPasswordViewModel
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recover_password)
        binding.submitButton.setOnClickListener { viewModel.onSubmitButtonClick() }
        binding.emailInput.observeChanges(viewModel::onEmailChanged)
        showHomeButton()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_send, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_send) {
            viewModel.onSubmitButtonClick()
        }
        return handleMenuItemClick(item) || super.onOptionsItemSelected(item)
    }

    override fun subscribeUi() {
        super.subscribeUi()
        viewModel.showEmailFieldError.observeEvent(this) { showEmailFieldError() }
        viewModel.showProgressIndicator.observeEvent(this, ::onNextProgressIndicator)
        viewModel.close.observeEvent(this) { if (it == true) close() }
    }


    private fun onNextProgressIndicator(shouldShow: Boolean?) {
        shouldShow?.let { binding.includedLoading.root.setVisible(shouldShow) }
    }

    private fun showEmailFieldError() {
        binding.emailInput.setError(R.string.validation_email)
    }

    private fun close() {
        finish()
    }

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, RecoverPasswordActivity::class.java)
        }
    }
}