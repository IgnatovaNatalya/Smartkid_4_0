package ru.mamsikgames.smartkid.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.mamsikgames.smartkid.data.dao.SmartDao
import ru.mamsikgames.smartkid.data.entity.LevelEntity
import ru.mamsikgames.smartkid.data.entity.RoundEntity
import ru.mamsikgames.smartkid.data.entity.UserEntity
import ru.mamsikgames.smartkid.data.entity.GroupLevelEntity


@Database(
    entities = [RoundEntity::class, UserEntity::class, LevelEntity::class, GroupLevelEntity::class],
    version = 4)
    //exportSchema = true,
    //autoMigrations = [
    //    AutoMigration(from = 3, to = 4)
    //]


abstract class SmartDb : RoomDatabase() {
    abstract fun smartDao(): SmartDao

//    companion object {
//
//        const val DB_NAME:String = "smart_db"
//
//        @Volatile
//        private var INSTANCE: SmartDb? = null
//
//        fun getInstance(context: Context): SmartDb {
//            synchronized(this) {
//                var instance = INSTANCE
//                if (instance == null) {
//                    instance = Room.databaseBuilder(context.applicationContext,
//                        SmartDb::class.java, DB_NAME
//                    )
//                        .createFromAsset("smart_db4.db")
//                        .build()
//                    INSTANCE = instance
//                }
//                return instance
//            }
//        }
//    }
}