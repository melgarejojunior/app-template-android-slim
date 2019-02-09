package br.com.melgarejo.apptemplateslim.domain.interactor.user

class InvalidFieldsException : Exception() {

    private val fields = HashSet<Int>()

    fun getFields(): MutableSet<Int> {
        return fields
    }

    fun addField(field: Int) {
        fields.add(field)
    }

    companion object {

        val NAME = 1
        val EMAIL = 2
        val PHONE_NUMBER = 3
        val CPF = 4
        val PASSWORD = 5
        val PASSWORD_CONFIRMATION = 6
    }
}
