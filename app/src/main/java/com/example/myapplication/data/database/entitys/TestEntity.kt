package com.example.myapplication.data.database.entitys

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TestEntity(
    @PrimaryKey val id:Int,
    val name:String
)