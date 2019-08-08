package br.com.melgarejo.apptemplateslim.presentation.util.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import br.com.melgarejo.apptemplateslim.R
import io.reactivex.Single
import io.reactivex.SingleEmitter
import pl.aprilapps.easyphotopicker.Constants
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File


private const val STARTED_FOR_RESULT = "STARTED_FOR_RESULT"

// intents

fun Activity.addStartedForResultFlag(intent: Intent): Intent {
    return intent.putExtra(STARTED_FOR_RESULT, true)
}

fun Activity.isStartedForResult(): Boolean {
    return intent.getBooleanExtra(STARTED_FOR_RESULT, false)
}

// appbar

fun AppCompatActivity.showHomeButton() {
    supportActionBar!!.setDisplayShowHomeEnabled(true)
    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
}

// menus

fun Activity.handleMenuItemClick(item: MenuItem): Boolean {
    return handleHomeButtonClick(item)
}

fun Activity.handleHomeButtonClick(item: MenuItem): Boolean {
    if (item.itemId == android.R.id.home) {
        finish()
        return true
    }
    return false
}

fun Activity.startEasyImageActivity() {
    if (EasyImage.canDeviceHandleGallery(this)) {
        EasyImage.openChooserWithGallery(this, this.getString(R.string.global_pick_avatar_using), 0)
    } else {
        EasyImage.openCamera(this, 0)
    }
}

fun easyImageWillHandleResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
    val chooserWithGalleryCode = Constants.RequestCodes.SOURCE_CHOOSER or Constants.RequestCodes.PICK_PICTURE_FROM_GALLERY
    return requestCode == chooserWithGalleryCode || EasyImage.willHandleActivityResult(requestCode, resultCode, data)
}

fun Activity.handleEasyImageResult(requestCode: Int, resultCode: Int, data: Intent?): Single<File> {
    return Single.create { emitter ->
        if (easyImageWillHandleResult(requestCode, resultCode, data)) {
            emitEasyImageResult(emitter, requestCode, resultCode, data)
        } else {
            emitter.onError(NotAnEasyImageIntentException())
        }
    }
}

private fun Activity.emitEasyImageResult(emitter: SingleEmitter<File>, requestCode: Int, resultCode: Int, data: Intent?) {
    EasyImage.handleActivityResult(requestCode, resultCode, data, this, object : DefaultCallback() {
        override fun onImagesPicked(imageFiles: List<File>, source: EasyImage.ImageSource, type: Int) {
            val file = imageFiles[0]
            file.scaleImageDown(1000, 1000)
            emitter.onSuccess(file)
        }

        override fun onImagePickerError(e: Exception, source: EasyImage.ImageSource?, type: Int) {
            emitter.onError(e)
        }
    })
}

//Toolbar

fun AppCompatActivity.setupToolbar(toolbar: Toolbar?, showHome: Boolean = true, title: String? = null) {
    if (title != null) {
        setupToolbarWithTitle(toolbar, title, showHome)
    } else {
        setupToolbar(toolbar, showHome)
    }
}

private fun AppCompatActivity.setupToolbar(toolbar: Toolbar?, showHome: Boolean) {
    toolbar?.let { setSupportActionBar(it) }
    supportActionBar?.run {
        setDisplayHomeAsUpEnabled(showHome)
        setDisplayShowHomeEnabled(showHome)
        setDisplayShowTitleEnabled(false)
    }
}

private fun AppCompatActivity.setupToolbarWithTitle(toolbar: Toolbar?, title: String, showHome: Boolean) {
    setSupportActionBar(toolbar)
    supportActionBar?.run {
        toolbar?.title = title
        setDisplayHomeAsUpEnabled(showHome)
        setDisplayShowHomeEnabled(showHome)
    }
}

//SoftKeyboard

fun AppCompatActivity.hideSoftKeyboard() {
    currentFocus?.let {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

fun AppCompatActivity.showSoftKeyboard() {
    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
}

// exceptions

class NotAnEasyImageIntentException : Exception()
