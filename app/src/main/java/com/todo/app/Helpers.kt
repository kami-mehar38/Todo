package com.todo.app

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.*

/**
 * Created by ingizly on 2019-09-24
 **/

class BaseViewModelFactory<T>(val creator: () -> T) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return creator() as T
    }
}

inline fun <reified T : ViewModel> Fragment.getViewModel(noinline creator: (() -> T)? = null): T {
    return if (creator == null)
        ViewModelProviders.of(this).get(T::class.java)
    else
        ViewModelProviders.of(this, BaseViewModelFactory(creator)).get(T::class.java)
}

inline fun <reified T : ViewModel> FragmentActivity.getViewModel(noinline creator: (() -> T)? = null): T {
    return if (creator == null)
        ViewModelProviders.of(this).get(T::class.java)
    else
        ViewModelProviders.of(this, BaseViewModelFactory(creator)).get(T::class.java)
}


internal class ItemTouchHelperCallbackService(private val context: Context, onItemSwipeListener: OnItemSwipeListener) :
    ItemTouchHelper.Callback(), AnkoLogger {


    private var swipeBack: Boolean = false
    private var moved = false
    private var isNotTriggered = true
    // The gesture threshold expressed in dp
    private val GESTURE_THRESHOLD_DP = ViewConfiguration.get(context).scaledTouchSlop
    private var mGestureThreshold: Int = 0


    private val xMark = ContextCompat.getDrawable(context, R.drawable.ic_delete)?.apply {
        setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
    }
    private val xMarkMargin = 40.toPx()


    // Get the screen's density scale
    private val scale: Float = Resources.getSystem().displayMetrics.density

    private var _itemTouchHelperAdapterListener: OnItemSwipeListener? = null

    init {
        this._itemTouchHelperAdapterListener = onItemSwipeListener
    }


    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (swipeBack) {
            swipeBack = false
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        // Set movement flags based on the layout manager
        return if (recyclerView.layoutManager is GridLayoutManager) {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            val swipeFlags = 0
            ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
        } else {
            val dragFlags = 0
            val swipeFlags = ItemTouchHelper.START
            ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        source: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        if (source.itemViewType != target.itemViewType) {
            return false
        }

        // Notify the adapter of the move
        //mAdapter.onItemMove(source.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
        // Notify the adapter of the dismissal
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            setOnTouchListener(recyclerView)

            // Fade out the view as it is swiped out of the parent's bounds
            val alpha = ALPHA_FULL - Math.abs(dX) / viewHolder.itemView.width.toFloat()
            viewHolder.itemView.alpha = alpha
            viewHolder.itemView.translationX = dX

            val background =
                ColorDrawable(ContextCompat.getColor(context, R.color.colorRed))
            background.setBounds(
                viewHolder.itemView.right + dX.toInt(),
                viewHolder.itemView.top,
                viewHolder.itemView.right,
                viewHolder.itemView.bottom
            )

            background.draw(c)

            xMark?.apply {
                val xt =
                    viewHolder.itemView.top + (viewHolder.itemView.bottom - viewHolder.itemView.top - xMark.intrinsicHeight) / 2
                setBounds(
                    viewHolder.itemView.right - xMarkMargin - xMark.intrinsicWidth,
                    xt,
                    viewHolder.itemView.right - xMarkMargin,
                    xt + xMark.intrinsicHeight
                )
                draw(c)
            }



            // Convert the dps to pixels, based on density scale
            mGestureThreshold = (GESTURE_THRESHOLD_DP * scale + Math.abs(dX)).toInt()
            info { mGestureThreshold.toString() }
            if (200.toPx() == minOf(200.toPx(), mGestureThreshold) && isNotTriggered) {
                isNotTriggered = !isNotTriggered
            } else if (200.toPx() > minOf(200.toPx(), mGestureThreshold) && !isNotTriggered) {
                isNotTriggered = true
            }

        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setOnTouchListener(
        recyclerView: RecyclerView
    ) {
        recyclerView.setOnTouchListener { _, event ->
            swipeBack = event?.action == MotionEvent.ACTION_CANCEL || event?.action == MotionEvent.ACTION_UP
            false
        }
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
            if (!isNotTriggered) {
                moved = true
            }
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        viewHolder.itemView.alpha = ALPHA_FULL
        if (moved) {
            _itemTouchHelperAdapterListener?.onItemSwiped(viewHolder.adapterPosition)
            moved = false
        }

    }

    companion object {
        private const val ALPHA_FULL = 1.0f
    }

    interface OnItemSwipeListener {
        fun onItemSwiped(position: Int)
    }
}

fun Int.toPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}

class GigDividerItemDecoration(val drawable: Drawable) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) == 0) {
            return
        }
        outRect.top = drawable.intrinsicHeight
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {

        val dividerLeft = (parent.paddingLeft + 16.toPx())
        val dividerRight = (parent.width - parent.paddingRight - 16.toPx())

        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val dividerTop = child.bottom + params.bottomMargin
            val dividerBottom = dividerTop + drawable.intrinsicHeight

            drawable.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
            drawable.draw(c)
        }

    }
}