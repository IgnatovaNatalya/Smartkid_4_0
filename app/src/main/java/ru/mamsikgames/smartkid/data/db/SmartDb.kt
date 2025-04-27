package ru.mamsikgames.smartkid.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.mamsikgames.smartkid.data.dao.SmartDao
import ru.mamsikgames.smartkid.data.entity.LevelGroupEntity
import ru.mamsikgames.smartkid.data.entity.LevelEntity
import ru.mamsikgames.smartkid.data.entity.RoundEntity
import ru.mamsikgames.smartkid.data.entity.UserEntity

@Database(
    entities = [RoundEntity::class, UserEntity::class, LevelEntity::class, LevelGroupEntity::class],
    version = 4)

abstract class SmartDb : RoomDatabase() {
    abstract fun smartDao(): SmartDao
}