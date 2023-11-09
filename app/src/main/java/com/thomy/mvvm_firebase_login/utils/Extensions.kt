package com.thomy.mvvm_firebase_login.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment


fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.disable() {
    isEnabled = false
}

fun View.enabled() {
    isEnabled = true
}

fun Fragment.toast(msg: String?) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
}

fun EditText.onTextChanged(listener: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            listener(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

fun EditText.loseFocusAfterAction(action: Int) {
    this.setOnEditorActionListener { v, actionId, _ ->
        if (actionId == action) {
            this.dismissKeyboard()
            v.clearFocus()
        }
        return@setOnEditorActionListener false
    }
}

fun View.dismissKeyboard(completed: () -> Unit = {}) {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val wasOpened = inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    if (!wasOpened) completed()
}

fun String.isValidEmail() =
    isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
