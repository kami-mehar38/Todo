package com.todo.app.todo.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.todo.app.GigDividerItemDecoration
import com.todo.app.R
import com.todo.app.addtodo.AddTodoActivity
import com.todo.app.getViewModel
import com.todo.app.todo.adapter.TodoAdapter
import com.todo.app.todo.model.Todo
import com.todo.app.todo.viewmodel.TodoViewModel
import com.todo.app.tododetails.view.TodoDetailsActivity
import kotlinx.android.synthetic.main.activity_todo.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast

class TodoActivity : AppCompatActivity(), TodoAdapter.OnItemDeleteListener {

    // request code for new to-do activity
    private val REQUEST_CODE: Int
        get() = 1001

    /**
     * DiffUtil callback for list adapter to update the list of recycler view items
     */
    private val diffUtil = object : DiffUtil.ItemCallback<Todo>() {
        override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem.compareTo(newItem) == 1
        }

        override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem.status == newItem.status && oldItem.description == newItem.description
        }
    }

    /**
     * ListAdapter for recycler view
     */
    private val todoAdapter: TodoAdapter by lazy {
        TodoAdapter(diffCallback = diffUtil) {
            startActivity<TodoDetailsActivity>(TodoDetailsActivity.EXTRA_TODO to it)
        }.apply {
            // setting stable ids to true to have smooth rendering of recycler view
            setHasStableIds(true)
            setOnItemDeleteListener(this@TodoActivity)
        }
    }

    private val todoViewModel: TodoViewModel? by lazy {
        getViewModel { TodoViewModel(application) }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)

        alert("Swipe left to delete item from the list.", "Info").show()

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(
            GigDividerItemDecoration(
                ContextCompat.getDrawable(
                    recyclerView.context,
                    R.drawable.divider
                )!!
            )
        )
        recyclerView.setHasFixedSize(true)
        recyclerView.setItemViewCacheSize(30)
        recyclerView.adapter = todoAdapter

        todoViewModel?.fetchTodoList()?.observe(this, Observer {
            if (it.isEmpty()) {
                tvNoData.visibility = View.VISIBLE
            } else {
                tvNoData.visibility = View.GONE
            }
            todoAdapter.submitList(it.reversed())
        })

        floatingActionButton.setOnClickListener {
            startActivityForResult<AddTodoActivity>(REQUEST_CODE)
        }
    }

    override fun onItemDelete(todo: Todo) {
        alert("Are you sure to delete", "Delete") {
            positiveButton("Delete") {
                todoViewModel?.delete(todo)
            }
            negativeButton("Cancel") {

            }
        }.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                val todo = it.getSerializableExtra(AddTodoActivity.EXTRA_REPLY) as Todo
                todoViewModel?.insert(todo)
            }
        } else {
            toast("Cancelled")
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
