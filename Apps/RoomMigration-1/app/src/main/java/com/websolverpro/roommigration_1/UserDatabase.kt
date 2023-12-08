package com.websolverpro.roommigration_1

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [User::class],
    exportSchema = true,
    version = 1,
//    autoMigrations = [
//        AutoMigration(1,2)
//    ]
)
abstract class UserDatabase: RoomDatabase() {
    abstract val dao: UserDao
}