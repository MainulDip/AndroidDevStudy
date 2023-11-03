package com.websolverpro.dagger2basics

import android.app.Application

class MyApplication: Application() {
    val appContainer = AppContainer()
}