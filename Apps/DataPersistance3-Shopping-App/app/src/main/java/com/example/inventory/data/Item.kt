package com.example.inventory.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.NumberFormat

@Entity(tableName = "item")
data class Item(
    @PrimaryKey(true) val id: Int = 0,
    @ColumnInfo("name") val itemName: String,
    @ColumnInfo("price") val itemPrice: Double,
    @ColumnInfo("quantity") val quantityInStock: Int
)

fun Item.getFormattedPrice(): String = NumberFormat.getCurrencyInstance().format(itemPrice)