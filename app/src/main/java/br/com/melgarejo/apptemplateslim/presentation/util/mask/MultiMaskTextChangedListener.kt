package br.com.melgarejo.apptemplateslim.presentation.util.mask

import android.widget.EditText
import java.util.*


class MultiMaskTextChangedListener(editText: EditText, reversed: Boolean, vararg masks: String) : BaseMaskTextChangedListener(editText, reversed) {

    private lateinit var masks: MutableList<Pair<Int, String>>

    constructor(editText: EditText, vararg masks: String) : this(editText, false, *masks) {}

    init {
        if (masks.size < 2) {
            throw RuntimeException("WTF!! Use MaskTextChangedListener if only need 1 mask!!!")
        }
        makeMaskList(*masks)
    }

    override fun applyMask(text: String?, reversed: Boolean): String? {
        val mask = chooseMask(text)
        return if (reversed) applyReversedMask(mask, text) else applyMask(mask, text!!)
    }

    private fun chooseMask(text: String?): String {
        val unmaskedText = textMasker.removeMask(text)
        for ((first, second) in masks) {
            if (unmaskedText!!.length <= first) {
                return second
            }
        }
        return masks[masks.size - 1].second
    }

    private fun makeMaskList(vararg masks: String) {
        this.masks = ArrayList()
        for (mask in masks) {
            this.masks.add(Pair(textMasker.getMaskTokenCount(mask), mask))
        }
        this.masks.sortWith(Comparator { lhs, rhs ->
            if (lhs.first == rhs.first) {
                throw RuntimeException("WTF!! How do you want me to know which one to use!?! Don't use two masks with the same length!!")
            }
            lhs.first - rhs.first
        })
    }
}
