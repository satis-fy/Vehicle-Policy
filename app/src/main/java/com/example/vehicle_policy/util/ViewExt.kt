package com.example.vehicle_policy.util

import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

fun View.mySnackbar(message: Int) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("dd/MM/yyyy")
    return format.format(date)
}