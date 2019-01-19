package br.com.melgarejo.apptemplateslim.domain.interactor.user


class SignUpForm {
    var email: String? = null
    var password: String? = null
    var name: String? = null
    var cpf: String? = null
    var phone: String? = null
    var passwordConfirmation: String? = null
    var avatarPath: String? = null

    fun useForm(action: (email: String,
                         password: String,
                         name: String,
                         cpf: String,
                         phone: String,
                         confirmationPassword: String,
                         avatarPath: String?) -> Unit): InvalidFieldsException? {
        email?.let { email ->
            password?.let { password ->
                passwordConfirmation?.let { confirmationPassword ->
                    name?.let { name ->
                        cpf?.let { cpf ->
                            phone?.let { phone ->
                                action.invoke(email, password, name, cpf, phone, confirmationPassword, avatarPath)
                                return null
                            }
                        }
                    }
                }
            }
        }
        return InvalidFieldsException().apply {
            if (email == null) addField(InvalidFieldsException.EMAIL)
            if (password == null) addField(InvalidFieldsException.PASSWORD)
            if (name == null) addField(InvalidFieldsException.NAME)
            if (cpf == null) addField(InvalidFieldsException.CPF)
            if (phone == null) addField(InvalidFieldsException.PHONE_NUMBER)
            if (passwordConfirmation == null) addField(InvalidFieldsException.PASSWORD_CONFIRMATION)
        }
    }
}