package br.com.melgarejo.apptemplateslim.presentation.structure.base

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.com.melgarejo.apptemplateslim.presentation.structure.navigation.NavData
import br.com.melgarejo.apptemplateslim.presentation.structure.arch.Event


open class BaseViewModel: LifecycleObserver, ViewModel() {
    val goTo: LiveData<Event<NavData>> get() = goToLiveData
    val dialog: LiveData<Event<DialogData>> get() = dialogLiveData
    val toast: LiveData<Event<String>> get() = toastLiveData
    val placeholder: LiveData<Placeholder> get() = placeholderLiveData

    private val goToLiveData = MutableLiveData<Event<NavData>>()
    private val dialogLiveData = MutableLiveData<Event<DialogData>>()
    private val placeholderLiveData = MutableLiveData<Placeholder>()
    private val toastLiveData = MutableLiveData<Event<String>>()

    fun setPlaceholder(placeholder: Placeholder) {
        placeholderLiveData.postValue(placeholder)
    }

    fun setPlaceholder(throwable: Throwable, retryAction: (() -> Unit)?) {
        setPlaceholder(errorHandler.getPlaceholder(throwable, retryAction))
    }

    fun setDialog(dialogData: DialogData) {
        dialogLiveData.postValue(Event(dialogData))
    }

    fun setToast(message: String) {
        toastLiveData.postValue(Event(message))
    }

    fun setDialog(
            throwable: Throwable, retryAction: (() -> Unit)? = null, onDismiss: (() -> Unit)? = null
    ) {
        setDialog(errorHandler.getDialogData(throwable, retryAction, onDismiss))
    }

    fun goTo(navData: NavData) {
        goToLiveData.postValue(Event(navData))
    }

}