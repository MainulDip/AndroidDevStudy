package com.websolverpro.dagger2basics

import com.websolverpro.dagger2basics.datasource.SingleUser
import dagger.Component

@Component(modules = [SingleUser::class])
interface ApplicationComponent {
    fun inject(activity: MainActivity)
}