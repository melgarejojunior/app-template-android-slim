package br.com.melgarejo.apptemplateslim.domain.boundary.resources


interface Logger {
    fun v(message: String)

    fun v(message: String, tr: Throwable)

    fun d(message: String)

    fun d(message: String, tr: Throwable)

    fun i(message: String)

    fun i(message: String, tr: Throwable)

    fun w(message: String)

    fun w(message: String, tr: Throwable)

    fun w(tr: Throwable)

    fun e(message: String)

    fun e(message: String, tr: Throwable)

    fun e(tr: Throwable)
}
