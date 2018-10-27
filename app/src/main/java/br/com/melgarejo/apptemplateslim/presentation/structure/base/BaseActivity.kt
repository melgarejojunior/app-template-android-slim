package br.com.melgarejo.apptemplateslim.presentation.structure.base

import android.support.v7.app.AppCompatActivity
import br.com.melgarejo.apptemplateslim.presentation.structure.sl.ServiceLocator


open class BaseActivity : AppCompatActivity() {

    protected val sl = ServiceLocator.getInstance(this.applicationContext)
}