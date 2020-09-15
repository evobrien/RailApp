package com.obregon.railapp.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.hideKeyboard() {
    view?.let {
        activity?.hideKeyboard(it)
    }
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun Context.getDrawableFromAttribute(attributeId: Int): Drawable {
    val typedValue = TypedValue().also { theme.resolveAttribute(attributeId, it, true) }
    return resources.getDrawable(typedValue.resourceId, theme)
}