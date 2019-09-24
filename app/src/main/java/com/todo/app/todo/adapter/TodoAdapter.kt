package com.todo.app.todo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.todo.app.App
import com.todo.app.ItemTouchHelperCallbackService
import com.todo.app.R
import com.todo.app.todo.model.Todo
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.find

/**
 * Created by ingizly on 2019-09-24
 **/
class TodoAdapter(
    diffCallback: DiffUtil.ItemCallback<Todo>,
    val onItemClickListener: (tot: Todo?) -> Unit
) :
    ListAdapter<Todo, TodoAdapter.ViewHolder>(diffCallback), AnkoLogger,
    ItemTouchHelperCallbackService.OnItemSwipeListener {

    private var onItemDeleteListener: OnItemDeleteListener? = null

    fun setOnItemDeleteListener(onItemDeleteListener: OnItemDeleteListener?) {
        this.onItemDeleteListener = onItemDeleteListener
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        val callback = ItemTouchHelperCallbackService(App.applicationContext()!!, this)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_todo, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemId(position: Int) = getItem(position)?.id?.toLong() ?: 0L

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var tvTitle: TextView? = null
        private var tvDescription: TextView? = null
        private var tvStatus: TextView? = null
        private var container: LinearLayout? = null

        init {
            tvTitle = view.find(R.id.tvTitle)
            tvDescription = view.find(R.id.tvDescription)
            tvStatus = view.find(R.id.tvStatus)
            container = view.find(R.id.container)
        }

        fun bind(todo: Todo) {
            tvTitle?.text = todo.title
            tvDescription?.text = todo.description
            if (todo.status == 0)
                tvStatus?.text = "Status: Incomplete"
            else if (todo.status == 1)
                tvStatus?.text = "Status: Completed"
            container?.setOnClickListener {
                onItemClickListener(getItem(adapterPosition))
            }
        }
    }

    override fun onItemSwiped(position: Int) {
        onItemDeleteListener?.onItemDelete(getItem(position))
    }

    interface OnItemDeleteListener {
        fun onItemDelete(todo: Todo)
    }
}