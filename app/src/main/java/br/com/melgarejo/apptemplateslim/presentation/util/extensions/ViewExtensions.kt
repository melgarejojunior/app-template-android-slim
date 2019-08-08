package br.com.melgarejo.apptemplateslim.presentation.util.extensions

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.google.android.material.textfield.TextInputLayout
import androidx.core.content.ContextCompat
import android.text.Editable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import br.com.melgarejo.apptemplateslim.R
import br.com.melgarejo.apptemplateslim.presentation.util.SimpleTextWatcher
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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



// views

fun View.setVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.isVisible(): Boolean {
    return visibility == View.VISIBLE
}

// images

fun ImageView.load(url: String) {
    load(url, placeholderRes = null)
}

fun ImageView.load(url: String, @DrawableRes placeholderRes: Int?, centerCrop: Boolean? = null) {
    load(url, placeholderRes, null, centerCrop)
}

fun ImageView.load(url: String, rounded: Boolean?, @DrawableRes placeholderRes: Int? = null) {
    load(url, placeholderRes, rounded, null)
}

fun ImageView.load(
        url: String, @DrawableRes placeholderRes: Int?,
        rounded: Boolean?,
        centerCrop: Boolean?
) {
    val placeholder =
            if (placeholderRes == null) null else ContextCompat.getDrawable(context, placeholderRes)
    load(url, placeholder, rounded, centerCrop)
}

fun ImageView.load(
        url: String,
        placeholderDrawable: Drawable?,
        rounded: Boolean?,
        centerCrop: Boolean?
) {
    val requestOptions = RequestOptions()
    if (placeholderDrawable != null) {
        requestOptions.placeholder(placeholderDrawable).error(placeholderDrawable)
    }
    if (rounded == true) {
        requestOptions.circleCrop()
    } else if (centerCrop == true) {
        requestOptions.centerCrop()
    } else {
        requestOptions.centerInside()
    }
    Glide.with(context).load(url).apply(requestOptions).into(this)
}

fun ImageView.centerInside(url: String?, @DrawableRes placeholderRes: Int? = null) {
    centerInside(url, placeholderRes?.let(context::drawableCompat))
}

fun ImageView.centerInside(url: String?, placeholder: Drawable?) {
    load(url, placeholderOptions(placeholder).centerInside())
}

fun ImageView.centerCrop(url: String?, @DrawableRes placeholderRes: Int? = null) {
    centerCrop(url, placeholderRes?.let(context::drawableCompat))
}

fun ImageView.centerCrop(url: String?, placeholder: Drawable?) {
    load(url, placeholderOptions(placeholder).centerCrop())
}

fun ImageView.circleCrop(url: String?, @DrawableRes placeholderRes: Int? = null) {
    circleCrop(url, placeholderRes?.let(context::drawableCompat))
}

fun ImageView.circleCrop(url: String?, placeholder: Drawable?) {
    load(url, placeholderOptions(placeholder).circleCrop())
}

fun ImageView.load(url: String?, options: RequestOptions) {
    if (url == null) {
        Glide.with(context).clear(this)
        options.placeholderDrawable?.let(this::setImageDrawable)
    } else {
        Glide.with(context).load(url).apply(options).into(this)
    }
}

// using drawables with glide loader is required
// glide 4.0 can't load vectors properly
// more recent versions (latest is 4.2) have issues merging dex files and throw exceptions at runtime
private fun placeholderOptions(placeholder: Drawable? = null): RequestOptions {
    val requestOptions = RequestOptions()
    placeholder?.let { requestOptions.placeholder(it).error(it) }
    return requestOptions
}