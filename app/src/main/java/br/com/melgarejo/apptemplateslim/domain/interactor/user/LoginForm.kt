package br.com.melgarejo.apptemplateslim.domain.interactor.user


class LoginForm {
    var email: String? = null
    var password: String? = null

    fun useForm(action: (email: String, password: String) -> Unit): InvalidFieldsException? {
        email?.let { email ->
            password?.let { password ->
                action.invoke(email, password)
                return null
            }
        }
        return InvalidFieldsException().apply {
            if (email == null) addField(InvalidFieldsException.EMAIL)
            if (password == null) addField(InvalidFieldsException.PASSWORD)
        }

    }
}