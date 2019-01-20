package br.com.melgarejo.apptemplateslim.presentation.util

import android.content.Context
import android.support.v7.widget.Toolbar
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import br.com.melgarejo.apptemplateslim.presentation.structure.base.BaseActivity

object ViewUtils {

    fun setupToolbar(activity: BaseActivity, toolbar: Toolbar?, showHome: Boolean) {
        toolbar?.let { activity.setSupportActionBar(it) }
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(showHome)
        activity.supportActionBar!!.setDisplayShowHomeEnabled(showHome)
        activity.supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    fun setupToolbarWithTitle(activity: BaseActivity, toolbar: Toolbar?, title: String, showHome: Boolean) {
        toolbar?.title = title
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(showHome)
        activity.supportActionBar!!.setDisplayShowHomeEnabled(showHome)

    }

    fun hideSoftKeyboard(activity: BaseActivity) {
        if (activity.currentFocus != null) {
            val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        }
    }

    fun showSoftKeyboard(activity: BaseActivity) {
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    }
}

