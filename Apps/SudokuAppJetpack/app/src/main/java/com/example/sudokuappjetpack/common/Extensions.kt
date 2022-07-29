package com.example.sudokuappjetpack.common

import android.app.Activity
import android.widget.Toast

// Utility Codes

internal fun Activity.makeToast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_LONG ).show()
}

internal fun Long.toTime(): String {
    if(this >= 3600) return "+59:59"
}