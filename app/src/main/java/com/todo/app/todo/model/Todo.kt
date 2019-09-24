package com.todo.app.todo.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.anko.AnkoLogger
import java.io.Serializable

/**
 * Created by ingizly on 2019-09-24
 **/

@Entity(tableName = "todo")
data class Todo(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var title: String,
    var description: String,
    var status: Int
) : AnkoLogger,
    Comparable<Todo>, Serializable, Cloneable {

    override fun compareTo(other: Todo): Int {
        return if (other.id == this.id)
            1 else 0
    }

    override fun toString(): String {
        return "id: $id, title: $title, description: $description, status: $status"
    }

    public override fun clone(): Any {
        return Todo(
            id = this.id,
            title = this.title,
            description = this.description,
            status = this.status
        )
    }
}