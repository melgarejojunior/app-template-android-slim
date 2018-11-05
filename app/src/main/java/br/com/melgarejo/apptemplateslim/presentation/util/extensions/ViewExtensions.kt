package br.com.melgarejo.apptemplateslim.presentation.util.extensions

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.design.widget.TextInputLayout
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import br.com.melgarejo.apptemplateslim.presentation.util.SimpleTextWatcher
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


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

// TextInputLayout

fun TextInputLayout.getText(): String? {
    return editText?.toString()
}

fun TextInputLayout.observeChanges(): Observable<String>? {
    return editText?.observeChanges()
}

// TextView

fun TextView.observeChanges(): Observable<String> {
    val subject = PublishSubject.create<String>()
    addTextChangedListener(object : SimpleTextWatcher() {
        override fun afterTextChanged(s: Editable) {
            subject.onNext(s.toString())
        }
    })
    return subject.hide()
}

fun TextView.setBoldText(fullText: String, boldPart: String) {
    this.setText(buildStringContent(fullText, boldPart), TextView.BufferType.SPANNABLE)
}

private fun buildStringContent(text: String, boldPart: String): SpannableStringBuilder {
    val spannableString = SpannableStringBuilder(text)
    val start =
            (if (text.indexOf(boldPart) < 0) null else text.indexOf(boldPart))
                    ?: return spannableString
    val end = start + boldPart.length
    spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return spannableString
}

