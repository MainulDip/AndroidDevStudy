package com.example.sudokuappjetpack.domain

enum class Difficulty(val modifier: Double) {
    EASY(0.5),
    MEDIUM(0.44),
    HARD(0.38)
}