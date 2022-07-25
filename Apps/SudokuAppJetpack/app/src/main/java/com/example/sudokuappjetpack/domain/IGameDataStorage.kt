package com.example.sudokuappjetpack.domain

import java.lang.Exception

interface IGameDataStorage  {
}

sealed class GameStorageResult {
    data class OnSuccess(val currentGame: SudokuPuzzle): GameStorageResult()
    data class OnError(val exception: Exception): GameStorageResult()
}