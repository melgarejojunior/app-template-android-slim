package br.com.melgarejo.apptemplateslim.presentation.util

import android.app.ProgressDialog
import android.content.Context
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.Toolbar
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import br.com.melgarejo.apptemplateslim.R
import br.com.melgarejo.apptemplateslim.presentation.structure.base.BaseActivity

object ViewUtils {

    fun setEditTextError(editText: EditText?, messageId: Int) {
        editText?.error = editText?.context?.getString(messageId)
        if (editText == null) return
        val hasErrorWatcher = editText.getTag(R.id.has_error_watcher) as? Boolean
        if (hasErrorWatcher == null || !hasErrorWatcher) {
            editText.addTextChangedListener(object : SimpleTextWatcher() {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                    editText.error = null
                    editText.removeTextChangedListener(this)
                    editText.setTag(R.id.has_error_watcher, false)
                }
            })
            editText.setTag(R.id.has_error_watcher, true)
        }
    }

    fun setTextInputLayoutError(textInputLayout: TextInputLayout, messageId: Int) {
        textInputLayout.error = textInputLayout.context.getString(messageId)
        if (textInputLayout.editText == null) return
        val hasErrorWatcher = textInputLayout.getTag(R.id.has_error_watcher) as? Boolean
        if (hasErrorWatcher == null || !hasErrorWatcher) {
            textInputLayout.editText!!.addTextChangedListener(object : SimpleTextWatcher() {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                    textInputLayout.error = null
                    textInputLayout.editText!!.removeTextChangedListener(this)
                    textInputLayout.setTag(R.id.has_error_watcher, false)
                }
            })
            textInputLayout.setTag(R.id.has_error_watcher, true)
        }
    }

    fun buildWaitDialog(context: Context): ProgressDialog {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage(context.getString(R.string.global_wait))
        return progressDialog
    }

    fun setupToolbar(activity: BaseActivity, toolbar: Toolbar?, showHome: Boolean) {
        activity.setSupportActionBar(toolbar)
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

