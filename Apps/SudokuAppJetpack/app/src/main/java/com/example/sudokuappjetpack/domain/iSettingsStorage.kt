package com.example.sudokuappjetpack.domain

import java.lang.Exception

interface iSettingsStorage {
}

sealed class SettingsStorageResult {
    data class OnSuccess(val settings: Settings) : SettingsStorageResult()
    data class OnError(val exception: Exception) : SettingsStorageResult()

}