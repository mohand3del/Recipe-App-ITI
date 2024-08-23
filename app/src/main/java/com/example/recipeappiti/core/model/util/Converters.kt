package com.example.recipeappiti.core.model.util

import androidx.room.TypeConverter

object Converters {
    @TypeConverter
    fun fromIntToBoolean(value: Int): Boolean {
        return value == 1
    }

    @TypeConverter
    fun fromStringToList(value: String?): List<String> {
        return if (value.isNullOrEmpty()) {
            emptyList()
        } else {
            value.split(",").map { it.trim() }
        }
    }

    @TypeConverter
    fun fromListToString(list: List<String>?): String {
        return list?.filter { it.isNotEmpty() }?.joinToString(",") ?: ""
    }
}