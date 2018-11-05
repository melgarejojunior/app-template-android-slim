package br.com.melgarejo.apptemplateslim.domain.interactor.user


class FormFields {

    private val invalidFieldsException: InvalidFieldsException = InvalidFieldsException()


    // getters

    var name: String? = null
        private set
    var email: String? = null
        private set
    var phoneNumber: String? = null
        private set
    var cpf: String? = null
        private set
    var password: String? = null
        private set
    var passwordConfirmation: String? = null
        private set

    // validations

    private val isNameValid: Boolean
        get() = name != null && !name!!.trim { it <= ' ' }.isEmpty()

    private val isEmailValid: Boolean
        get() = email != null && email!!.matches(EMAIL_PATTERN.toRegex())

    private val isPhoneNumberValid: Boolean
        get() = phoneNumber != null && phoneNumber!!.matches(PHONE_NUMBER_PATTERN.toRegex())

    private val isCpfValid: Boolean
        get() = cpf != null && cpf!!.matches(CPF_PATTERN.toRegex())

    private val isPasswordValid: Boolean
        get() = password != null && !password!!.isEmpty()

    private val isPasswordConfirmationValid: Boolean
        get() = if (passwordConfirmation == null) password == null else passwordConfirmation == password

    val isValid: Boolean
        get() = invalidFieldsException.fields.isEmpty()

    // other

    val exception: InvalidFieldsException?
        get() = if (isValid) null else invalidFieldsException

    // builders

    fun withName(name: String): FormFields {
        this.name = name
        updateField(InvalidFieldsException.NAME, isNameValid)
        return this
    }

    fun withEmail(email: String): FormFields {
        this.email = email
        updateField(InvalidFieldsException.EMAIL, isEmailValid)
        return this
    }

    fun withPhoneNumber(phoneNumber: String): FormFields {
        this.phoneNumber = phoneNumber
        updateField(InvalidFieldsException.PHONE_NUMBER, isPhoneNumberValid)
        return this
    }

    fun withCpf(cpf: String): FormFields {
        this.cpf = cpf
        updateField(InvalidFieldsException.CPF, isCpfValid)
        return this
    }

    fun withPassword(password: String): FormFields {
        this.password = password
        updateField(InvalidFieldsException.PASSWORD, isPasswordValid)
        return this
    }

    fun withPasswordConfirmation(passwordConfirmation: String): FormFields {
        this.passwordConfirmation = passwordConfirmation
        updateField(InvalidFieldsException.PASSWORD_CONFIRMATION, isPasswordConfirmationValid)
        return this
    }

    private fun updateField(field: Int, valid: Boolean) {
        if (valid) {
            invalidFieldsException.fields.remove(field)
        } else {
            invalidFieldsException.fields.add(field)
        }
    }

    companion object {

        private val EMAIL_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+"
        private val CPF_PATTERN = "(\\d{3}.\\d{3}.\\d{3}-\\d{2})"
        private val PHONE_NUMBER_PATTERN = "(\\(\\d{2}\\) \\d{4,5}-\\d{4})"
    }
}
