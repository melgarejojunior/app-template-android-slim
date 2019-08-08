package br.com.melgarejo.apptemplateslim.presentation.structure.base

import androidx.lifecycle.*
import br.com.melgarejo.apptemplateslim.presentation.structure.arch.Event
import br.com.melgarejo.apptemplateslim.presentation.structure.navigation.NavData
import br.com.melgarejo.apptemplateslim.presentation.structure.sl.ServiceLocator
import br.com.melgarejo.apptemplateslim.presentation.util.ErrorHandler
import br.com.melgarejo.apptemplateslim.presentation.util.viewmodels.DialogData
import br.com.melgarejo.apptemplateslim.presentation.util.viewmodels.Placeholder
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel : LifecycleObserver, ViewModel() {
    val goTo: LiveData<Event<NavData>> get() = goToLiveData
    val dialog: LiveData<Event<DialogData>> get() = dialogLiveData
    val toast: LiveData<Event<String>> get() = toastLiveData
    val placeholder: LiveData<Placeholder> get() = placeholderLiveData

    private val goToLiveData = MutableLiveData<Event<NavData>>()
    private val dialogLiveData = MutableLiveData<Event<DialogData>>()
    private val placeholderLiveData = MutableLiveData<Placeholder>()
    private val toastLiveData = MutableLiveData<Event<String>>()
    private val errorHandler = ServiceLocator.getInstance().get(ErrorHandler::class.java)

    protected val disposables: CompositeDisposable = CompositeDisposable()

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

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestroy() {
        disposables.dispose()
    }
}