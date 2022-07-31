package com.example.sudokuappjetpack.persistence

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.bracketcove.sudokuappjetpack.GameSettings
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

// GameSettings is comming from proto directory's game_settings.proto
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