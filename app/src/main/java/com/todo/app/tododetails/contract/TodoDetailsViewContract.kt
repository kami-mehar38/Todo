package com.todo.app.tododetails.contract

import com.todo.app.todo.model.Todo
import kotlinx.coroutines.Job

/**
 * Created by ingizly on 2019-09-24
 **/
interface TodoDetailsViewContract {

    /**
     * This function updates the multiple or single object of to-do
     *
     * @return Job instance for the coroutines
     */
    fun update(todo: Todo): Job
}