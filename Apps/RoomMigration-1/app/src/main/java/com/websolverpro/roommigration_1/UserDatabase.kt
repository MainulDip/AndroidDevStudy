package com.websolverpro.roommigration_1

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [User::class, School::class],
    exportSchema = true,
    version = 4,
    autoMigrations = [
        AutoMigration(1,2),
        AutoMigration(2,3, UserDatabase.Migration2To3::class)
    ]
)
abstract class UserDatabase: RoomDatabase() {
    abstract val dao: UserDao

    /**
     * Defining AutoMigrationSpec
     */
    @RenameColumn("User", "created", "createdAt")
    class Migration2To3: AutoMigrationSpec

    /**
     * Defining Manual MMigration
     */
    companion object {
        val migration3To4 = object : Migration(3,4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS school (name CHAR NOT NULL PRIMARY KEY)")
            }
        }
    }
}