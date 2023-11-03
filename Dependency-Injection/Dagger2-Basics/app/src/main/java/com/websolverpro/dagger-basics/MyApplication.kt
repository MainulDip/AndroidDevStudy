package com.websolverpro.`dagger-basics`

import android.app.Application

class MyApplication: Application() {
    val appContainer = AppContainer()
}