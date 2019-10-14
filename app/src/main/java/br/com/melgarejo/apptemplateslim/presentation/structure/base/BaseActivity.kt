package br.com.melgarejo.apptemplateslim.presentation.structure.base

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.melgarejo.apptemplateslim.presentation.structure.navigation.NavData
import br.com.melgarejo.apptemplateslim.presentation.structure.navigation.Navigator
import br.com.melgarejo.apptemplateslim.presentation.structure.sl.ServiceLocator
import br.com.melgarejo.apptemplateslim.presentation.util.extensions.observeEvent
import br.com.melgarejo.apptemplateslim.presentation.util.extensions.shortToast
import br.com.melgarejo.apptemplateslim.presentation.util.extensions.showDialog
import br.com.melgarejo.apptemplateslim.presentation.util.viewmodels.DialogData

abstract class BaseActivity : AppCompatActivity() {

    abstract val sl: ServiceLocator
    abstract val baseViewModel: BaseViewModel

    private var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeUi()
    }

    open fun subscribeUi() {
        baseViewModel.dialog.observeEvent(this, ::onNextDialog)
        baseViewModel.goTo.observeEvent(this, ::onNextNavigation)
        baseViewModel.toast.observeEvent(this, ::onNextToast)
    }

    private fun onNextToast(text: String?) {
        text?.let {
            shortToast(it)
        }
    }

    private fun onNextDialog(dialogData: DialogData?) {
        dialog?.dismiss()
        dialog = dialogData?.let { showDialog(it) }
    }

    private fun onNextNavigation(navData: NavData?) {
        navData?.let {
            Navigator.goTo(this, it)
        }
    }
}