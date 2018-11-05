package br.com.melgarejo.apptemplateslim.presentation.util.resources

import io.reactivex.schedulers.Schedulers
import io.reactivex.Scheduler
import br.com.melgarejo.apptemplateslim.domain.util.SchedulerProvider
import io.reactivex.android.schedulers.AndroidSchedulers


class DefaultSchedulerProvider : SchedulerProvider {
    override fun main(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    override fun io(): Scheduler {
        return Schedulers.io()
    }

    override fun computation(): Scheduler {
        return Schedulers.computation()
    }
}
