package br.com.melgarejo.apptemplateslim.presentation.landing

import android.os.Bundle
import br.com.melgarejo.apptemplateslim.presentation.structure.base.BaseActivity

class SplashActivity : BaseActivity() {

    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = sl.get(SplashViewModel::class.java)
        lifecycle.addObserver(viewModel)
    }
}