package com.todo.app.todo.constract

import androidx.lifecycle.LiveData
import com.todo.app.todo.model.Todo
import kotlinx.coroutines.Job

/**
 * Created by ingizly on 2019-09-24
 **/
interface TodoViewContract {
    /**
     * This function fetches the to-do list from database
     *
     * @return LiveData of To-do List
     */
    fun fetchTodoList(): LiveData<List<Todo>>

    /**
     * This function inserts a new to-do item in the database
     *
     * @return Job instance for the coroutines
     */
    fun insert(todo: Todo): Job

    /**
     * This function deletes a  to-do item from the database
     *
     * @return Job instance for the coroutines
     */
    fun delete(todo: Todo): Job
}