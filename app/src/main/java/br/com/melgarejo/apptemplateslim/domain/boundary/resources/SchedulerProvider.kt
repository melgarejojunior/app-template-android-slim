package br.com.melgarejo.apptemplateslim.domain.boundary.resources

import io.reactivex.Scheduler

interface SchedulerProvider {
    fun main(): Scheduler

    fun io(): Scheduler

    fun computation(): Scheduler
}
