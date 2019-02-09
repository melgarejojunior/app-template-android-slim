package br.com.melgarejo.apptemplateslim.presentation.util.resources

import android.content.Context
import android.util.Log
import br.com.melgarejo.apptemplateslim.BuildConfig
import br.com.melgarejo.apptemplateslim.R
import br.com.melgarejo.apptemplateslim.domain.boundary.resources.Logger


class AndroidLogger constructor(context: Context) : Logger {

    private val tag = context.getString(R.string.app_name)

    override fun v(message: String) {
        if (BuildConfig.DEBUG) Log.v(tag, message)
    }

    override fun v(message: String, tr: Throwable) {
        if (BuildConfig.DEBUG) Log.v(tag, message, tr)
    }

    override fun d(message: String) {
        if (BuildConfig.DEBUG) Log.d(tag, message)
    }

    override fun d(message: String, tr: Throwable) {
        if (BuildConfig.DEBUG) Log.d(tag, message, tr)
    }

    override fun i(message: String) {
        if (BuildConfig.DEBUG) Log.i(tag, message)
    }

    override fun i(message: String, tr: Throwable) {
        if (BuildConfig.DEBUG) Log.i(tag, message, tr)
    }

    override fun w(message: String) {
        if (BuildConfig.DEBUG) Log.w(tag, message)
    }

    override fun w(message: String, tr: Throwable) {
        if (BuildConfig.DEBUG) Log.w(tag, message, tr)
    }

    override fun w(tr: Throwable) {
        if (BuildConfig.DEBUG) Log.w(tag, tr.message, tr)
    }

    override fun e(message: String) {
        if (BuildConfig.DEBUG) Log.e(tag, message)
    }

    override fun e(message: String, tr: Throwable) {
        if (BuildConfig.DEBUG) Log.e(tag, message, tr)
    }

    override fun e(tr: Throwable) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, tr.message, tr)
        } else {
//             TODO ADD CRASHLYTICS
//            Crashlytics.log(tr.message)
        }
    }
}
