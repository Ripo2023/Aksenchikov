package com.example.myapplication.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.data.database.entitys.TestEntity

@Database(
    version = 1,
    entities = [TestEntity::class]
)
abstract class AppDatabase : RoomDatabase()