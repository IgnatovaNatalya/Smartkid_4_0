package ru.mamsikgames.smartkid.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.mamsikgames.smartkid.data.db.dao.SmartDao
import ru.mamsikgames.smartkid.data.db.entity.Operation
import ru.mamsikgames.smartkid.data.db.entity.Round
import ru.mamsikgames.smartkid.data.db.entity.User


@Database(
    entities = [Round::class, User::class, Operation::class],
    version = 4,
    exportSchema = true//,
    //autoMigrations = [
    //    AutoMigration(from = 3, to = 4)
    //]
)

abstract class SmartDb : RoomDatabase() {
    abstract fun smartDao(): SmartDao

    companion object {

        const val DB_NAME:String = "smart_db"

        @Volatile
        private var INSTANCE: SmartDb? = null

        fun getInstance(context: Context): SmartDb {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext,
                        SmartDb::class.java, DB_NAME
                    )
                        .createFromAsset("smart_db4.db")
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}