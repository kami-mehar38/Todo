package com.todo.app.todo.repository

import androidx.lifecycle.LiveData
import com.todo.app.todo.model.Todo
import com.todo.app.todo.roomdatabase.TodoDao

/**
 * Created by ingizly on 2019-09-24
 **/
class TodoRepository(private val todoDao: TodoDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allTodos: LiveData<List<Todo>> = todoDao.getAllTodos()


    suspend fun insert(todo: Todo) {
        todoDao.insert(todo)
    }

    suspend fun delete(todo: Todo) {
        todoDao.delete(todo)
    }

    suspend fun update(todo: Todo) {
        todoDao.update(todo)
    }
}