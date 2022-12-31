package com.example.android.unscramble.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {
// ViewModel is an abstract class. In Java/Kotlin, abstract class can have constructors


    private var _score = 0
    val score get() = _score
    private var _currentWordCount = 0
    val currentWordCount get() = _currentWordCount
    private var _currentScrambledWord = "test"
    val currentScrambledWord get() = _currentScrambledWord

    init {
        Log.d("GameFragment", "GameViewModel Created (From GameViewModel)")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel destroyed! (From GameViewModel)")
    }
}