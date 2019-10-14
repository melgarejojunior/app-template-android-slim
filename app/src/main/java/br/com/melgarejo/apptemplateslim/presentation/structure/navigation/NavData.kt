package br.com.melgarejo.apptemplateslim.presentation.structure.navigation

import android.content.Context
import android.content.Intent

interface NavData {
    fun createIntent(context: Context): Intent
}