package com.websolverpro.manual_dependency_injection

import android.app.Application

class MyApplication: Application() {
    val appContainer = AppContainer()
}