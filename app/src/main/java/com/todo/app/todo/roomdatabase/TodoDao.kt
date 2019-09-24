package com.todo.app.todo.roomdatabase

import androidx.lifecycle.LiveData
import androidx.room.*
import com.todo.app.todo.model.Todo

/**
 * Created by ingizly on 2019-09-24
 **/
@Dao
interface TodoDao {

    @Query("SELECT * from todo")
    fun getAllTodos(): LiveData<List<Todo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(todo: Todo)

    @Delete
    suspend fun delete(todo: Todo)

    @Update
    suspend fun update(todo: Todo)


    // For testing purpose
    @Insert
    fun insertAll(vararg todo: Todo)

    @Query("SELECT * FROM todo WHERE title = :title LIMIT 1")
    fun findByTitle(title: String): Todo
}