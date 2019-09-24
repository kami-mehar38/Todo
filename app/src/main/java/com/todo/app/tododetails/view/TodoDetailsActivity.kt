package com.todo.app.tododetails.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.todo.app.R
import com.todo.app.getViewModel
import com.todo.app.todo.model.Todo
import com.todo.app.tododetails.viewmodel.TodoDetailsViewModel
import kotlinx.android.synthetic.main.activity_todo_details.*
import org.jetbrains.anko.toast

class TodoDetailsActivity : AppCompatActivity() {

    companion object {
        val EXTRA_TODO: String
            get() = "EXTRA_TODO"
    }

    private var todo: Todo? = null

    private val todoDetailsViewModel: TodoDetailsViewModel? by lazy {
        getViewModel { TodoDetailsViewModel(application) }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_details)

        supportActionBar?.let {
            it.title = "Todo Details"
        }

        intent.extras?.let {
            todo = it.getSerializable(EXTRA_TODO) as Todo

            todo?.let { todo ->
                tvTitle.text = todo.title
                tvDescription.setText(todo.description)
                checkBox.isChecked = todo.status == 1
            }
        }

        btnUpdate.setOnClickListener {
            if (todo == null)
                toast("Failed to update")
            else {
                todo?.let {
                    val newTodo = it.clone() as Todo
                    if (checkBox.isChecked)
                        newTodo.status = 1
                    else newTodo.status = 0

                    newTodo.description = tvDescription.text.toString()
                    todoDetailsViewModel?.update(newTodo)
                    toast("Updated")
                }
            }
        }
    }
}
