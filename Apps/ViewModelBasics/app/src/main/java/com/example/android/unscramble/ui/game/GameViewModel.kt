package com.example.android.unscramble.ui.game

import android.content.ContentProvider
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.internal.ContextUtils.getActivity
import kotlinx.coroutines.currentCoroutineContext

class GameViewModel: ViewModel() {
// ViewModel is an abstract class. In Java/Kotlin, abstract class can have constructors


    private var _score = 0
    val score get() = _score
    private var _currentWordCount = 0
    val currentWordCount get() = _currentWordCount
    private val _currentScrambledWord = MutableLiveData<String>()
    val currentScrambledWord: LiveData<String> get() = _currentScrambledWord

    var wordsList = mutableListOf<String>()

    lateinit var currentWord: String

    init {
        Log.d("GameFragment", "GameViewModel Created (From GameViewModel)")
        getNextWord()
    }

    private fun getNextWord(skipping: Boolean = false) {
        currentWord = allWordsList.random()
        var tempWord: CharArray = currentWord.toCharArray()
        tempWord.shuffle()

        while (tempWord.toString().equals(currentWord, false)) {
            tempWord.shuffle()
        }

        if (wordsList.contains(currentWord)) {
            getNextWord()
        } else {
            _currentScrambledWord.value = String(tempWord)

            if(!skipping) {
                ++_currentWordCount
            }
            wordsList.add(currentWord)
        }
    }

    fun nextWord(shouldSkip: Boolean = false): Boolean {
        return if (currentWordCount < MAX_NO_OF_WORDS) {
            getNextWord(shouldSkip)
            true
        } else false
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel destroyed! (From GameViewModel)")
    }

    private fun increaseScore() {
        _score += SCORE_INCREASE
    }

    fun isUserWordCorrect(playerWord: String): Boolean {
        if(playerWord.equals(currentWord, true)){
            increaseScore()
            return true
        }
        return false
    }

    fun reinitializeData() {
        _score = 0
        _currentWordCount = 0
        wordsList.clear()
        getNextWord()
    }
}