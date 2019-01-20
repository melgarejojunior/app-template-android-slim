package br.com.melgarejo.apptemplateslim.presentation.util.resources

import android.content.Context
import br.com.melgarejo.apptemplateslim.domain.boundary.resources.Cache
import br.com.melgarejo.apptemplateslim.presentation.structure.navigation.Navigator


class LoginAction constructor(
        private val context: Context,
        private val cache: Cache
) {
    fun execute() {
        cache.clear()
        Navigator.goToLogin(context, true)
    }
}
