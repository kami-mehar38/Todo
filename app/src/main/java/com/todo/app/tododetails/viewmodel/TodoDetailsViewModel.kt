package com.todo.app.tododetails.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.todo.app.todo.model.Todo
import com.todo.app.todo.repository.TodoRepository
import com.todo.app.todo.roomdatabase.TodoRoomDatabase
import com.todo.app.tododetails.contract.TodoDetailsViewContract
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Created by ingizly on 2019-09-24
 **/
class TodoDetailsViewModel(application: Application): AndroidViewModel(application), TodoDetailsViewContract {

    private val repository: TodoRepository

    init {
        // Gets reference to WordDao from WordRoomDatabase to construct
        // the correct WordRepository.
        val todoDao = TodoRoomDatabase.getDatabase(application, viewModelScope).todoDao()
        repository = TodoRepository(todoDao)

    }

    override fun update(todo: Todo): Job = viewModelScope.launch {
        repository.update(todo)
    }
}