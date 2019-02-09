package br.com.melgarejo.apptemplateslim.presentation.util.extensions

import br.com.melgarejo.apptemplateslim.domain.extensions.defaultConsumers
import br.com.melgarejo.apptemplateslim.presentation.util.viewmodels.Placeholder
import io.reactivex.Observable
import io.reactivex.Single


fun <T> Single<T>.defaultPlaceholders(placeholderPlacerAction: (Placeholder) -> (Unit)): Single<T> {
    return this.defaultConsumers({ placeholderPlacerAction(Placeholder.Loading()) }, { placeholderPlacerAction(Placeholder.HideAll) })
}

fun <T> Observable<T>.defaultPlaceholders(placeholderPlacerAction: (Placeholder) -> (Unit)): Observable<T> {
    return this.defaultConsumers({ placeholderPlacerAction(Placeholder.Loading()) }, { placeholderPlacerAction(Placeholder.HideAll) })
}
