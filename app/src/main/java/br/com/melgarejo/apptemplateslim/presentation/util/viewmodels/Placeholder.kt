package br.com.melgarejo.apptemplateslim.presentation.util.viewmodels


sealed class Placeholder(
        val messageVisible: Boolean,
        val message: String?,
        val buttonVisible: Boolean,
        val buttonText: String?,
        val progressVisible: Boolean,
        val buttonAction: (() -> Unit)?
) {

    class Loading(message: String? = null) :
            Placeholder(message != null, message, false, null, true, null)

    class Message(message: String) : Placeholder(true, message, false, null, false, null)

    class Action(message: String?, buttonText: String, action: (() -> Unit)?) :
            Placeholder(true, message, true, buttonText, false, action)

    object HideAll : Placeholder(false, null, false, null, false, null)

    fun visible(): Boolean {
        return messageVisible || buttonVisible || progressVisible
    }

    fun onActionButtonClicked() {
        buttonAction?.invoke()
    }
}
