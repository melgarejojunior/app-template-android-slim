package br.com.melgarejo.apptemplateslim.presentation.util.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import br.com.melgarejo.apptemplateslim.presentation.AppTemplateSlimApplication
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

// shortcuts

val Activity.appTemplateSlimApplication get() = application as AppTemplateSlimApplication

val Fragment.appCompatActivity get() = activity as AppCompatActivity?

val RecyclerView.ViewHolder.context: Context get() = itemView.context

fun Intent.shouldClearTask(clearTask: Boolean) {
    if (clearTask) {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}

fun File.scaleImageDown(width: Int, height: Int) {
    val boundsOptions = BitmapFactory.Options()
    boundsOptions.inJustDecodeBounds = true
    BitmapFactory.decodeFile(absolutePath, boundsOptions)
    if (boundsOptions.outWidth <= width && boundsOptions.outHeight <= height) return

    val original = BitmapFactory.decodeFile(absolutePath)
    val cropped: Bitmap
    cropped = if (original.width >= original.height) {
        Bitmap.createBitmap(
                original,
                original.width / 2 - original.height / 2,
                0,
                original.height,
                original.height)
    } else {
        Bitmap.createBitmap(
                original,
                0,
                original.height / 2 - original.width / 2,
                original.width,
                original.width)
    }
    val rescaled = Bitmap.createScaledBitmap(cropped, width, height, false)
    val outputStream = BufferedOutputStream(FileOutputStream(this))
    rescaled.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
    outputStream.close()
}

