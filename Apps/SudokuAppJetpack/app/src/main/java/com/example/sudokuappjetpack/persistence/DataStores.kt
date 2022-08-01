package com.example.sudokuappjetpack.persistence

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import com.protobuff.sudokuappjetpack.GameSettings
import com.protobuff.sudokuappjetpack.Statistics
import java.io.InputStream
import java.io.OutputStream

// GameSettings is coming from proto directory's game_settings.proto
internal val Context.settingsDataStore: DataStore<GameSettings> by dataStore(
    "game_settings.pb",
    serializer = GameSettingsSerializer
)

private object GameSettingsSerializer: Serializer<GameSettings> {
    override val defaultValue: GameSettings
        get() = GameSettings.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): GameSettings {
        try {
            return GameSettings.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto buffer file.", exception)
        }
    }

    override suspend fun writeTo(t: GameSettings, output: OutputStream) = t.writeTo(output)

}

internal val Context.statsDataStore: DataStore<Statistics> by dataStore(
    "user_statistics.pd",
    StatisticsSerializer
)

private object StatisticsSerializer: Serializer<Statistics> {
    override val defaultValue: Statistics
        get() = Statistics.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Statistics {
        try {
            return Statistics.parseFrom(input)
        }catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto statistics file", exception)
        }
    }

    override suspend fun writeTo(t: Statistics, output: OutputStream) = t.writeTo(output)
}