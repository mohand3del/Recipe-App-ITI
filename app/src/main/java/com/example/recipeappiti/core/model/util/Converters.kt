package com.example.recipeappiti.core.model.util

import androidx.room.TypeConverter

object Converters {
    @TypeConverter
    fun fromIntToBoolean(value: Int): Boolean {
        return value == 1
    }

    @TypeConverter
    fun fromStringToArray(value: String?): Array<String> {
        return value?.split(",")?.toTypedArray() ?: arrayOf()
    }

    @TypeConverter
    fun fromArrayToString(array: Array<String>?): String {
        return array?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun fromStringToList(value: String?): List<String> {
        return value?.split(",") ?: emptyList()
    }

    @TypeConverter
    fun fromListToString(list: List<String>?): String {
        return list?.joinToString(",") ?: ""
    }
}