package br.com.melgarejo.apptemplateslim.presentation.structure.navigation


sealed class NavData {
    object MainNavData : NavData()
    object RecoverPasswordNavData : NavData()
    object SignUpNavData : NavData()
}