package br.com.melgarejo.apptemplateslim.presentation.password.recover

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
import br.com.melgarejo.apptemplateslim.presentation.util.ActivityUtils
import br.com.melgarejo.apptemplateslim.presentation.util.ViewUtils
import br.com.melgarejo.apptemplateslim.presentation.util.extensions.observeChanges
import br.com.melgarejo.apptemplateslim.presentation.util.extensions.observeEvent
import br.com.melgarejo.apptemplateslim.presentation.util.extensions.setVisible


class RecoverPasswordActivity : BaseActivity() {
    override val baseViewModel: BaseViewModel get() = viewModel

    private lateinit var sl: ServiceLocator
    private lateinit var binding: ActivityRecoverPasswordBinding
    private lateinit var viewModel: RecoverPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        sl = ServiceLocator.getInstance(applicationContext)
        viewModel = sl.get(RecoverPasswordViewModel::class) as RecoverPasswordViewModel
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recover_password)
        binding.submitButton.setOnClickListener { viewModel.onSubmitButtonClick() }
        binding.emailInput.observeChanges()?.subscribe { viewModel.onEmailChanged(it) }
        ActivityUtils.showHomeButton(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_send, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_send) {
            viewModel.onSubmitButtonClick()
        }
        return ActivityUtils.handleMenuItemClick(this, item) || super.onOptionsItemSelected(item)
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
        ViewUtils.setTextInputLayoutError(binding.emailInput, R.string.validation_email)
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
