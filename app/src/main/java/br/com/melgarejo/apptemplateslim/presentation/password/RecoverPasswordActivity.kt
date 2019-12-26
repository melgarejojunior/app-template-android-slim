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
import br.com.melgarejo.apptemplateslim.presentation.structure.navigation.InstanceMaker
import br.com.melgarejo.apptemplateslim.presentation.util.extensions.handleMenuItemClick
import br.com.melgarejo.apptemplateslim.presentation.util.extensions.observe
import br.com.melgarejo.apptemplateslim.presentation.util.extensions.observeChanges
import br.com.melgarejo.apptemplateslim.presentation.util.extensions.observeEvent
import br.com.melgarejo.apptemplateslim.presentation.util.extensions.setError
import br.com.melgarejo.apptemplateslim.presentation.util.extensions.showHomeButton
import br.com.melgarejo.apptemplateslim.presentation.util.viewmodels.Placeholder


class RecoverPasswordActivity : BaseActivity() {
    override val baseViewModel: BaseViewModel get() = viewModel


    private lateinit var binding: ActivityRecoverPasswordBinding
    private lateinit var viewModel: RecoverPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = InstanceMaker.get()
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
        viewModel.placeholder.observe(this, ::onNextPlaceholder)
        viewModel.close.observeEvent(this) { if (it == true) close() }
    }


    private fun onNextPlaceholder(placeholder: Placeholder?) {
        placeholder?.let { binding.includedLoading.placeholder = it }
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