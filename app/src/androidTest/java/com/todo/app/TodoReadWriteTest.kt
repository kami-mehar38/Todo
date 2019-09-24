package com.todo.app

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.runner.AndroidJUnit4
import com.todo.app.todo.model.Todo
import com.todo.app.todo.roomdatabase.TodoDao
import com.todo.app.todo.roomdatabase.TodoRoomDatabase
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import org.hamcrest.CoreMatchers.`is` as Is

/**
 * Created by ingizly on 2019-09-24
 **/
@RunWith(AndroidJUnit4::class)
class TodoReadWriteTest {
    private lateinit var todoDao: TodoDao
    private lateinit var db: TodoRoomDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getContext()
        db = Room.inMemoryDatabaseBuilder(
            context, TodoRoomDatabase::class.java
        ).build()
        todoDao = db.todoDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    /**
     * This test will check if findByTitle get the same to-do item that's saved in database,
     * and the one that gets from the database
     */

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {
        val todo = Todo(title = "My Title", description = "My description", status = 0)
        todoDao.insertAll(todo)
        val todoItem = todoDao.findByTitle(todo.title)
        assertThat(todoItem.title, Is(equalTo(todo.title)))
    }

    // We can have more tests to check for other functionality, but due to time
    // constraint im implementing just one test
}