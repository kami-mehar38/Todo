package com.todo.app.addtodo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.todo.app.R
import com.todo.app.todo.model.Todo
import kotlinx.android.synthetic.main.activity_add_todo.*
import org.jetbrains.anko.toast

class AddTodoActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_REPLY = "NEW_TODO"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo)

        supportActionBar?.let {
            it.title = "Add Todo"
        }

        button2.setOnClickListener {
            when {
                editText.text.isEmpty() -> toast("Enter title")
                editText2.text.isEmpty() -> toast("Enter details")
                else -> {
                    val replyIntent = Intent()
                    val todo = Todo(title = editText.text.toString(), description = editText2.text.toString(), status = 0)
                    replyIntent.putExtra(EXTRA_REPLY, todo)
                    setResult(Activity.RESULT_OK, replyIntent)
                    finish()
                }
            }
        }
    }
}
