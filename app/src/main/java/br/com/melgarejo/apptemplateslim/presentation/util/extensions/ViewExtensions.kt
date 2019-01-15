package br.com.melgarejo.apptemplateslim.presentation.util.extensions

import android.support.annotation.StringRes
import android.support.design.widget.TextInputLayout
import android.text.Editable
import android.view.View
import android.widget.TextView
import br.com.melgarejo.apptemplateslim.R
import br.com.melgarejo.apptemplateslim.presentation.util.SimpleTextWatcher
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject


// TextInputLayout
fun TextInputLayout.getText(): String? {
    return editText?.toString()
}

fun TextInputLayout.observeChanges(callback: (String) -> Unit) {
    editText?.observeChanges(callback)
}

fun TextInputLayout.setError(@StringRes messageId: Int?) {
    messageId?.let {
        error = context.getString(messageId)
        if (editText == null) return
        val hasErrorWatcher: Boolean? = getTag(R.id.has_error_watcher) as? Boolean
        if (hasErrorWatcher == null || !hasErrorWatcher) {
            editText?.addTextChangedListener(object : SimpleTextWatcher() {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                    error = null
                    editText?.removeTextChangedListener(this)
                    setTag(R.id.has_error_watcher, false)
                }
            })
            setTag(R.id.has_error_watcher, true)
        }
        return
    }
    error = null
}


// TextView
fun TextView.observeChanges(callback: (String) -> Unit): Disposable? {
    val subject = PublishSubject.create<String>()
    addTextChangedListener(object : SimpleTextWatcher() {
        override fun afterTextChanged(s: Editable) {
            subject.onNext(s.toString())
        }
    })
    return subject.subscribe { callback(it) }
}

//View
fun View.setOnClickListener(callback: () -> Unit) {
    this.setOnClickListener { callback.invoke() }
}
