package com.android.l2l.twolocal.utils


import android.widget.EditText
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.onTextChanged
import com.google.android.material.textfield.TextInputLayout

fun EditText.notEmpty(message: String): Boolean {
    val str: String = text.toString()
    if (str.isBlank())
        setEditTextError(message)
    return str.isNotBlank()
}


fun EditText.validateEmail(): Boolean {
    val str = text.toString()
    var ret = false

    if (InputValidationRegex.isValidateEmail(str)) ret = true else setEditTextError(R.string.invalid_email)

    return ret
}


private fun getInpLayout(txt: EditText): TextInputLayout? {
    var input: TextInputLayout? =null
    if (txt.parent?.parent is TextInputLayout)
        input = txt.parent.parent as TextInputLayout

    return input
}


fun EditText.removeEditTextError() {
    this.error = null
    val inpLayout: TextInputLayout? = getInpLayout(this)
    inpLayout?.let {
        it.error = null
        it.isErrorEnabled = false
        it.background?.let {
            background.colorFilter = null
        }
    }
}


fun EditText.setEditTextError(ErrorMsgID: Int) {
    setEditTextError(context.getString(ErrorMsgID))
}

fun EditText.setEditTextError(errMsg: String?) {
    this.requestFocus()

    val inpLayout: TextInputLayout? = getInpLayout(this)
    if (inpLayout != null) {
        inpLayout.error = errMsg
        inpLayout.setErrorIconDrawable(0)
    } else error = errMsg

    onTextChanged {
        removeEditTextError()
    }
}

fun TextInputLayout.removeTextInputError() {
    this.error = null
    this.isErrorEnabled = false
}

fun TextInputLayout.setTextInputError(errMsg: String?) {
    requestFocus()
    error = errMsg
}
