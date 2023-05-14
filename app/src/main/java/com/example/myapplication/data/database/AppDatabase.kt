package com.example.myapplication.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.data.database.entitys.Test

@Database(
    version = 1,
    entities = [Test::class]
)
abstract class AppDatabase : RoomDatabase()