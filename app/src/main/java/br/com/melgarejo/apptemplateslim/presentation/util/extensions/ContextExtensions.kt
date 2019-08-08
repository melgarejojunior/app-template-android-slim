package br.com.melgarejo.apptemplateslim.presentation.util.extensions

import android.app.AlertDialog
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.IntentFilter
import android.net.Uri
import android.provider.Settings
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.webkit.URLUtil
import android.widget.Toast
import br.com.melgarejo.apptemplateslim.R
import br.com.melgarejo.apptemplateslim.presentation.util.viewmodels.DialogData

fun Context.showDialog(dialogData: DialogData): Dialog {
    val builder = AlertDialog.Builder(this)
    builder.setTitle(dialogData.title)
    builder.setMessage(dialogData.message)
    if (dialogData.confirmButtonText == null && dialogData.onConfirm == null) {
        builder.setPositiveButton(dialogData.dismissButtonText, dialogData.onDismiss)
    } else {
        builder.setPositiveButton(
            dialogData.confirmButtonText, dialogData.onConfirm
                ?: dialogData.onDismiss
        )
        if (dialogData.dismissButtonText != null || dialogData.onDismiss != null) {
            builder.setNegativeButton(dialogData.dismissButtonText, dialogData.onDismiss)
        }
    }
    dialogData.onDismiss?.let { builder.setOnCancelListener { it() } }
    builder.setCancelable(dialogData.cancelable ?: true)
    return builder.show()
}

fun AlertDialog.Builder.setPositiveButton(buttonText: String?, onClick: (() -> Unit)?) =
    setPositiveButton(
        buttonText ?: context.getString(R.string.global_ok),
        onClick?.let { { _: DialogInterface, _: Int -> it() } }
    )

fun AlertDialog.Builder.setNegativeButton(buttonText: String?, onClick: (() -> Unit)?) =
    setNegativeButton(
        buttonText ?: context.getString(R.string.global_cancel),
        onClick?.let { { _: DialogInterface, _: Int -> it() } }
    )

fun Context.shortToast(@StringRes messageId: Int) = shortToast(getString(messageId))

fun Context.shortToast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.longToast(@StringRes messageId: Int) = longToast(getString(messageId))

fun Context.longToast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()

// resources

fun Context.colorCompat(@ColorRes colorId: Int) = ContextCompat.getColor(this, colorId)

fun Context.drawableCompat(@DrawableRes drawableId: Int) =
    ContextCompat.getDrawable(this, drawableId)

//Broadcast Receivers

fun Context.registerLocalReceiver(
    action: String,
    callback: (context: Context?, intent: Intent?) -> Unit
): BroadcastReceiver {
    val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(c: Context?, i: Intent?) = callback(c, i)
    }
    val filter = IntentFilter(action)
    LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, filter)
    return broadcastReceiver
}

fun Context.unregisterLocalReceiver(broadcastReceiver: BroadcastReceiver) {
    LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
}

//Open browser
fun Context.openBrowser(url: String) {

    val formattedUrl = if (URLUtil.isHttpUrl(url) || URLUtil.isHttpsUrl(url)) {
        url
    } else {
        "http://$url"
    }

    val browserIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(formattedUrl)
    ).apply { addFlags(FLAG_ACTIVITY_NEW_TASK) }
    startActivity(browserIntent)
}

fun Context.openApplicationDetailsSettings() {
    startActivity(Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).apply { addFlags(FLAG_ACTIVITY_NEW_TASK) })
}
