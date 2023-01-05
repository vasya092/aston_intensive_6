package com.example.astonintensive5.utils

import android.content.Context
import android.os.IBinder
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

fun Context.makeToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
}
fun View.makeToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this.context, text, duration).show()
}

fun Context.hideKeyboard(windowToken: IBinder?) {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}