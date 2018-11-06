package br.com.melgarejo.apptemplateslim.presentation.util


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import br.com.melgarejo.apptemplateslim.R
import br.com.melgarejo.apptemplateslim.presentation.util.extensions.scaleImageDown
import io.reactivex.Single
import io.reactivex.SingleEmitter
import pl.aprilapps.easyphotopicker.Constants
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File

object ActivityUtils {

    private val STARTED_FOR_RESULT = "STARTED_FOR_RESULT"

    // intents

    fun addStartedForResultFlag(intent: Intent): Intent {
        return intent.putExtra(STARTED_FOR_RESULT, true)
    }

    fun isStartedForResult(activity: Activity): Boolean {
        return activity.intent.getBooleanExtra(STARTED_FOR_RESULT, false)
    }

    // appbar

    fun showHomeButton(activity: AppCompatActivity) {
        activity.supportActionBar!!.setDisplayShowHomeEnabled(true)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    // menus

    fun handleMenuItemClick(activity: Activity, item: MenuItem): Boolean {
        return handleHomeButtonClick(activity, item)
    }

    fun handleHomeButtonClick(activity: Activity, item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            activity.finish()
            return true
        }
        return false
    }

    fun startEasyImageActivity(activity: Activity) {
        if (EasyImage.canDeviceHandleGallery(activity)) {
            EasyImage.openChooserWithGallery(activity, activity.getString(R.string.global_pick_avatar_using), 0)
        } else {
            EasyImage.openCamera(activity, 0)
        }
    }

    fun easyImageWillHandleResult(requestCode: Int, resultCode: Int, data: Intent): Boolean {
        val chooserWithGalleryCode = Constants.RequestCodes.SOURCE_CHOOSER or Constants.RequestCodes.PICK_PICTURE_FROM_GALLERY
        return requestCode == chooserWithGalleryCode || EasyImage.willHandleActivityResult(requestCode, resultCode, data)
    }

    fun handleEasyImageResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent): Single<File> {
        return Single.create { emitter ->
            if (easyImageWillHandleResult(requestCode, resultCode, data)) {
                emitEasyImageResult(activity, emitter, requestCode, resultCode, data)
            } else {
                emitter.onError(NotAnEasyImageIntentException())
            }
        }
    }

    private fun emitEasyImageResult(activity: Activity, emitter: SingleEmitter<File>, requestCode: Int, resultCode: Int, data: Intent) {
        EasyImage.handleActivityResult(requestCode, resultCode, data, activity, object : DefaultCallback() {
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

    // exceptions

    class NotAnEasyImageIntentException : Exception()


    fun openExternalLink(context: Context, url: String) {
        context.startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)))
    }

}
