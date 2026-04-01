package com.example.serviconnect.utils

import android.graphics.Color
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun View.show() { visibility = View.VISIBLE }
fun View.hide() { visibility = View.GONE }

fun Fragment.toast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Snackbar.showError(message: String) {
    this.setText(message).setBackgroundTint(Color.RED).show()
}