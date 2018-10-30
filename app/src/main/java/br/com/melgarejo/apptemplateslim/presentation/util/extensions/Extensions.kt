package br.com.melgarejo.apptemplateslim.presentation.util.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import br.com.melgarejo.apptemplateslim.presentation.AppTemplateSlimApplication

// shortcuts

val Activity.agrobrazilApplication get() = application as AppTemplateSlimApplication

val Fragment.appCompatActivity get() = activity as AppCompatActivity?

val RecyclerView.ViewHolder.context: Context get() = itemView.context

fun Intent.shouldClearTask(clearTask: Boolean) {
    if (clearTask) {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}


