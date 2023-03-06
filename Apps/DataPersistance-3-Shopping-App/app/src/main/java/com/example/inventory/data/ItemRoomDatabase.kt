package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class ItemRoomDatabase: RoomDatabase() {

    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var INSTANCE: ItemRoomDatabase? = null

        fun getDatabase(context: Context): ItemRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, ItemRoomDatabase::class.java, "item_database")
                    .fallbackToDestructiveMigration()
                    .build()

                /**
                 * migration object with a migration strategy is required for when the schema changes
                 * A migration object is an object that defines how to take all rows with the old schema and convert them to rows in the new schema, so that no data is lost.
                 * fallbackToDestructiveMigration() migration strategy will destroy and rebuild the database, which means that the previous data is lost
                */

                INSTANCE = instance
                return instance
            }
        }
    }
}