package com.example.sudokuappjetpack.common

abstract class BaseLogic<T> {
    protected lateinit var jobTracker: Job
    abstract fun onEvent(event: T)
}