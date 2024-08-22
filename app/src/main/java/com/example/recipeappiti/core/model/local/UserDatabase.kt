package com.example.recipeappiti.core.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.recipeappiti.core.model.util.Converters

@Database(entities = [User::class], version = 1)
@TypeConverters(Converters::class)
abstract class UserDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null
        fun getDatabaseInstance(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context = context,
                    klass = UserDatabase::class.java,
                    name = "recipes_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}