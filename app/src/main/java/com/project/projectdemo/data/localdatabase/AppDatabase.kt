package com.project.projectdemo.data.localdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.project.projectdemo.data.entities.User

@Database(
    entities = [User::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase(){
    abstract fun getUserDao() : UserDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context?): AppDatabase? {
            synchronized(this) {
                var instance = this.instance
                if (instance == null) {
                    if (context != null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "UserDatabase.db"
                        )
                            .build()
                    }
                }
                return instance
            }
        }
    }
}