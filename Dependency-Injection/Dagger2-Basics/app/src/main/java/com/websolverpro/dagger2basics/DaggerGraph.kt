package com.websolverpro.dagger2basics

import dagger.Component

@Component
interface ApplicationComponent {
    fun inject(activity: MainActivity)
}