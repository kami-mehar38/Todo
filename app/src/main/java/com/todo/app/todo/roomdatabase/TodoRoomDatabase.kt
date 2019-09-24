package com.todo.app.todo.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.todo.app.todo.model.Todo
import kotlinx.coroutines.CoroutineScope

/**
 * Created by ingizly on 2019-09-24
 **/

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [Todo::class], version = 1)
abstract class TodoRoomDatabase: RoomDatabase() {

    abstract fun todoDao(): TodoDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: TodoRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): TodoRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoRoomDatabase::class.java,
                    "word_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}