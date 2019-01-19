package br.com.melgarejo.apptemplateslim.presentation.signup

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import br.com.melgarejo.apptemplateslim.R
import br.com.melgarejo.apptemplateslim.databinding.ActivityRegisterBinding
import br.com.melgarejo.apptemplateslim.domain.interactor.user.InvalidFieldsException
import br.com.melgarejo.apptemplateslim.presentation.structure.base.BaseActivity
import br.com.melgarejo.apptemplateslim.presentation.structure.base.BaseViewModel
import br.com.melgarejo.apptemplateslim.presentation.structure.navigation.Navigator
import br.com.melgarejo.apptemplateslim.presentation.structure.sl.ServiceLocator
import br.com.melgarejo.apptemplateslim.presentation.util.extensions.*
import br.com.melgarejo.apptemplateslim.presentation.util.mask.InputMask


class SignUpActivity : BaseActivity() {

    override val sl: ServiceLocator get() = ServiceLocator.getInstance(this.applicationContext)
    override val baseViewModel: BaseViewModel get() = viewModel

    private lateinit var viewModel: SignUpViewModel
    private lateinit var binding: ActivityRegisterBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = sl.get(SignUpViewModel::class) as SignUpViewModel
        lifecycle.addObserver(viewModel)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        setupUi()
        super.onCreate(savedInstanceState)
    }

    private fun setupUi() {
        with(binding) {
            InputMask.apply(cpfInput, CPF_MASK)
            InputMask.apply(phoneNumberInput, PHONE_MASK_9_DIGITS, PHONE_MASK_8_DIGITS)
            nameInput.observeChanges(viewModel::onNameChanged)
            emailInput.observeChanges(viewModel::onEmailChanged)
            cpfInput.observeChanges(viewModel::onCpfChanged)
            phoneNumberInput.observeChanges(viewModel::onPhoneChanged)
            passwordInput.observeChanges(viewModel::onPasswordChanged)
            passwordConfirmationInput.observeChanges(viewModel::onPasswordConfirmationChanged)
            submitButton.setOnClickListener(viewModel::onSubmitClicked)
            uploadImage.setOnClickListener(viewModel::onAvatarClicked)
            uploadText.setOnClickListener(viewModel::onAvatarClicked)
        }
    }

    override fun subscribeUi() {
        super.subscribeUi()
        viewModel.errors.observeEvent(this, ::onNextErrors)
        viewModel.goToMain.observe(this, this::onNextGoToMain)

    }

    private fun onNextGoToMain(shouldGo: Boolean?) {
        shouldGo?.let { Navigator.goToMain(this, true) }
    }

    private fun onNextErrors(errors: InvalidFieldsException?) {
        errors?.getFields()?.let { showFieldError(it.toList()) }
    }

    private fun showFieldError(fields: List<Int>) {
        fields.forEach { field ->
            getTextInputLayout(field)?.setError(getErrorMessageId(field))
        }
    }


    private fun getTextInputLayout(field: Int): TextInputLayout? {
        return when (field) {
            FIELD_NAME -> binding.nameInput
            FIELD_EMAIL -> binding.emailInput
            FIELD_PHONE_NUMBER -> binding.phoneNumberInput
            FIELD_CPF -> binding.cpfInput
            FIELD_PASSWORD -> binding.passwordInput
            FIELD_PASSWORD_CONFIRMATION -> binding.passwordConfirmationInput
            else -> null
        }
    }

    private fun getErrorMessageId(field: Int): Int {
        return when (field) {
            FIELD_NAME -> R.string.validation_name
            FIELD_EMAIL -> R.string.validation_email
            FIELD_PHONE_NUMBER -> R.string.validation_phone_number
            FIELD_CPF -> R.string.validation_cpf
            FIELD_PASSWORD -> R.string.validation_password
            FIELD_PASSWORD_CONFIRMATION -> R.string.validation_password_confirmation
            else -> 0
        }
    }


    companion object {

        private const val CPF_MASK = "###.###.###-##"
        private const val PHONE_MASK_9_DIGITS = "(##) #####-####"
        private const val PHONE_MASK_8_DIGITS = "(##) ####-####"
        private const val FIELD_NAME = 1
        private const val FIELD_EMAIL = 2
        private const val FIELD_PHONE_NUMBER = 3
        private const val FIELD_CPF = 4
        private const val FIELD_PASSWORD = 5
        private const val FIELD_PASSWORD_CONFIRMATION = 6

        fun createIntent(context: Context): Intent {
            return Intent(context, SignUpActivity::class.java)
        }
    }
}