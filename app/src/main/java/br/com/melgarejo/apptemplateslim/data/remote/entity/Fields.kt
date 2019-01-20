package br.com.melgarejo.apptemplateslim.data.remote.entity

class Fields : HashMap<String, String?>() {

    fun and(key: String, value: String?): Fields {
        put(key, value)
        return this
    }

    companion object {

        fun with(key: String, value: String?): Fields {
            return Fields().and(key, value)
        }
    }
}
