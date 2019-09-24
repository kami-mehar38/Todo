package com.todo.app.todo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.todo.app.todo.constract.TodoViewContract
import com.todo.app.todo.model.Todo
import com.todo.app.todo.repository.TodoRepository
import com.todo.app.todo.roomdatabase.TodoRoomDatabase
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Created by ingizly on 2019-09-24
 **/
class TodoViewModel(application: Application) : AndroidViewModel(application), TodoViewContract {


    // The ViewModel maintains a reference to the repository to get data.
    private val repository: TodoRepository

    init {
        // Gets reference to WordDao from WordRoomDatabase to construct
        // the correct WordRepository.
        val todoDao = TodoRoomDatabase.getDatabase(application, viewModelScope).todoDao()
        repository = TodoRepository(todoDao)
    }

    override fun fetchTodoList(): LiveData<List<Todo>> {
        return repository.allTodos
    }

    override fun insert(todo: Todo): Job = viewModelScope.launch {
        repository.insert(todo)
    }

    override fun delete(todo: Todo): Job = viewModelScope.launch {
        repository.delete(todo)
    }

}