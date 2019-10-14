package br.com.melgarejo.apptemplateslim.presentation.util.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import br.com.melgarejo.apptemplateslim.presentation.structure.arch.Event
import br.com.melgarejo.apptemplateslim.presentation.structure.arch.EventObserver


fun <T> defaultMutableLiveData(t: T?): MutableLiveData<T> {
    val liveData = MutableLiveData<T>()
    liveData.value = t
    return liveData
}

fun <T> LiveData<T>.observe(owner: LifecycleOwner, observer: (T?) -> Unit) {
    observe(owner, androidx.lifecycle.Observer { observer(it) })
}

fun <T> LiveData<Event<T>>.observeEvent(owner: LifecycleOwner, observer: (T?) -> Unit) {
    observe(owner, EventObserver(observer))
}

fun <T> LiveData<T>.reobserve(owner: LifecycleOwner, observer: (T?) -> Unit) {
    removeObservers(owner)
    observe(owner, observer)
}

fun <T, U> LiveData<T>.map(mapper: (T?) -> U) = Transformations.map(this, mapper)
