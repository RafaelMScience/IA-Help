package com.rafaelm.iahelp.functions

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class Mask {
    companion object {
        private fun replaceChars(maskFull: String): String {
            return maskFull.replace(".", "").replace("-", "")
                .replace("(", "").replace(")", "")
                .replace("/", "").replace(" ", "")
                .replace("*", "")
        }


        fun mask(mask: String, edtMask: EditText): TextWatcher {

            return object : TextWatcher {
                var isUpdating: Boolean = false
                var oldString: String = ""
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    val str = replaceChars(s.toString())
                    var cpfWithMask = ""

                    if (count == 0)//is deleting
                        isUpdating = true

                    if (isUpdating) {
                        oldString = str
                        isUpdating = false
                        return
                    }

                    var i = 0
                    for (m: Char in mask.toCharArray()) {
                        if (m != '#' && str.length > oldString.length) {
                            cpfWithMask += m
                            continue
                        }
                        try {
                            cpfWithMask += str[i]
                        } catch (e: Exception) {
                            break
                        }
                        i++
                    }

                    isUpdating = true
                    edtMask.setText(cpfWithMask)
                    edtMask.setSelection(cpfWithMask.length)

                }

                override fun afterTextChanged(editable: Editable) {

                }
            }
        }
    }
}