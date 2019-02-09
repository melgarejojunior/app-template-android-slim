package br.com.melgarejo.apptemplateslim.presentation.util.extensions

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import br.com.melgarejo.apptemplateslim.presentation.structure.arch.Event
import br.com.melgarejo.apptemplateslim.presentation.structure.arch.EventObserver


fun <T> defaultMutableLiveData(t: T?): MutableLiveData<T> {
    val liveData = MutableLiveData<T>()
    liveData.value = t
    return liveData
}

fun <T> LiveData<T>.observe(owner: LifecycleOwner, observer: (T?) -> Unit) {
    observe(owner, android.arch.lifecycle.Observer { observer(it) })
}

fun <T> LiveData<Event<T>>.observeEvent(owner: LifecycleOwner, observer: (T?) -> Unit) {
    observe(owner, EventObserver(observer))
}

fun <T> LiveData<T>.reobserve(owner: LifecycleOwner, observer: (T?) -> Unit) {
    removeObservers(owner)
    observe(owner, observer)
}

fun <T, U> LiveData<T>.map(mapper: (T?) -> U) = Transformations.map(this, mapper)
