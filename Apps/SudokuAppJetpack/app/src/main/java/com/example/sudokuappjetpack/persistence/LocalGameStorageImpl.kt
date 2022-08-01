package com.example.sudokuappjetpack.persistence

import com.example.sudokuappjetpack.domain.GameStorageResult
import com.example.sudokuappjetpack.domain.IGameDataStorage
import com.example.sudokuappjetpack.domain.SudokuPuzzle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutputStream

private const val FILE_NAME = "game_state.txt"

class LocalGameStorageImpl ( fileStorageDirectory: String,
    private val pathToStorageFile: File = File(fileStorageDirectory, FILE_NAME)
        ) : IGameDataStorage
{

    override suspend fun updateGame(game: SudokuPuzzle): GameStorageResult = withContext(Dispatchers.IO) {
        try {
            updateGameData(game)
            GameStorageResult.OnSuccess(game)
        }catch (e: Exception) {
            GameStorageResult.OnError(e)
        }
    }

    private fun updateGameData(game: SudokuPuzzle) {
        try {
            val fileOutputStream = FileOutputStream(pathToStorageFile)
            val objectInputStream = ObjectOutputStream(fileOutputStream)
            objectInputStream.writeObject(game)
            objectInputStream.close()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun updateNode(x: Int, y: Int, elapseTime: Long): GameStorageResult {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrentGame(): GameStorageResult {
        TODO("Not yet implemented")
    }

}