package com.example.inventory.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    /*
    * onConflict params directs room what to do in case of a conflict
    * The OnConflictStrategy.IGNORE strategy ignores a new item if it's primary key is already in the database.
    */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    /*
    * For Flow return type, room runs the query on the background thread
    * no need to make explicit suspend function
    */
    @Query("SELECT * from item WHERE id = :id")
    fun getItem(id: Int): Flow<Item>

    @Query("SELECT * from item ORDER BY name ASC")
    fun getItems(): Flow<List<Item>>
}