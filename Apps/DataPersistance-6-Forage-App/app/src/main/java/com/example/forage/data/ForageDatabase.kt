/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.forage.data

import android.app.Application
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.forage.model.Forageable
import kotlin.coroutines.coroutineContext
import android.content.Context
import kotlinx.coroutines.currentCoroutineContext

/**
 * Room database to persist data for the Forage app.
 * This database stores a [Forageable] entity
 */
// TODO: create the database with all necessary annotations, methods, variables, etc.

@Database(entities = [Forageable::class], version = 1, exportSchema = false)
abstract class ForageDatabase : RoomDatabase() {

    // from ui, when creating (delegating) the viewModel using activityViewModels
    // we'll instantiate the viewModelFactory and inject DAO using this method through lazy
    // the database using lazy from a class that inherits Activity
    // database using lazy {}
    abstract fun getForageableDao(): ForageableDao

    companion object {
        @Volatile
        private var INSTANCE: ForageDatabase? = null

        // this method needs to be called from a class that extends the Application
        // we will pass context from there

        fun getDatabase(context: Context): ForageDatabase {
            try {
                return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(context.applicationContext, ForageDatabase::class.java, "Forage-Database")
                        .build()

//                    val instance = Room.databaseBuilder(context.getApplicationContext)

                    INSTANCE = instance
                    instance
                }
            } catch (e: Exception) {
                throw IllegalArgumentException(e)
            }
        }
    }
}
