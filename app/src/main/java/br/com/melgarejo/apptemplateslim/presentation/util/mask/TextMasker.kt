package br.com.melgarejo.apptemplateslim.presentation.util.mask


class TextMasker {

    fun maskTextReversed(mask: String?, optText: String?): String? {
        if (isEmpty(optText) || isEmpty(mask)) return optText
        var maskedText = ""
        optText?.let { text ->
            mask?.let { mask ->
                val maskCharacters = mask.toCharArray()
                var textCharIndex = text.length - 1
                for (maskCharIndex in maskCharacters.indices.reversed()) {
                    val maskCharacter = maskCharacters[maskCharIndex]
                    if (maskCharacter == '#') {
                        maskedText = text[textCharIndex] + maskedText
                        textCharIndex--
                        if (textCharIndex < 0) {
                            if (text.length >= getMaskTokenCount(mask)) {
                                maskedText = mask.substring(0, maskCharIndex) + maskedText
                            }
                            break
                        }
                    } else {
                        maskedText = maskCharacter + maskedText
                    }
                }
            }
        }
        return maskedText
    }

    fun maskText(mask: String, text: String?): String? {
        if (isEmpty(text)) return text
        var maskedText = ""
        text?.let {
            var textCharIndex = 0
            val maskCharacters = mask.toCharArray()
            val maskLength = maskCharacters.size
            for (maskCharIndex in 0 until maskLength) {
                val maskCharacter = maskCharacters[maskCharIndex]
                if (maskCharacter == '#') {
                    maskedText += text[textCharIndex]
                    textCharIndex++
                    if (textCharIndex >= text.length) {
                        break
                    }
                } else {
                    maskedText += maskCharacter
                }
            }
        }
        return maskedText
    }

    fun getMaskTokenCount(mask: String): Int {
        return removeMask(mask)!!.length
    }

    fun removeMask(text: String?): String? {
        return text?.replace("[.\\-/() +]".toRegex(), "")
    }

    fun isEmpty(text: String?): Boolean {
        return text == null || text.isEmpty()
    }
}
