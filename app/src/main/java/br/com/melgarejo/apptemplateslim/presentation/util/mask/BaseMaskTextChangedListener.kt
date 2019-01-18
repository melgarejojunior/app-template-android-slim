package br.com.melgarejo.apptemplateslim.presentation.util.mask

import android.widget.EditText
import br.com.melgarejo.apptemplateslim.presentation.util.SimpleTextWatcher


abstract class BaseMaskTextChangedListener @JvmOverloads constructor(private val editText: EditText, private val reversed: Boolean = false) : SimpleTextWatcher() {
    private var isUpdating: Boolean = false
    private var oldText: String? = null
    protected val textMasker = TextMasker()

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (this.isUpdating) {
            this.isUpdating = false
        } else {
            var text = textMasker.removeMask(s.toString())
            if (text?.length != this.oldText?.length) {
                val maskedText = applyMask(text, reversed)
                text = textMasker.removeMask(maskedText)
                this.isUpdating = true
                this.editText.setText(maskedText)
                maskedText?.let { this.editText.setSelection(it.length) }
            }
            this.oldText = text
        }
    }

    protected abstract fun applyMask(text: String?, reversed: Boolean): String?

    companion object {


        fun applyReversedMask(mask: String?, text: String?): String? {
            if (text == null) return null
            val textMasker = TextMasker()
            return textMasker.maskTextReversed(mask, textMasker.removeMask(text))
        }

        fun applyMask(mask: String, text: String?): String? {
            if (text == null) return null
            val textMasker = TextMasker()
            return textMasker.maskText(mask, textMasker.removeMask(text))
        }
    }

}
