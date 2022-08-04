package com.example.homeworkyandex

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private var swipeBackgroundDelete: ColorDrawable = ColorDrawable(Color.parseColor("#161618"))
    private lateinit var deleteIcon: Drawable
    private lateinit var doneIcon: Drawable

    private lateinit var deletedItem: Task
    private var deletedPosition: Int = 0

    lateinit var taskAdapter: TaskAdapter

    val mDataSet = ArrayList<Task>()
    lateinit var recycler: RecyclerView

    @JvmName("getRecycler1")
    fun getRecycler(): RecyclerView {
        setContentView(R.layout.activity_main)
        return findViewById(R.id.recycler)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // do not work
        findViewById<ImageButton>(R.id.add_task).setOnClickListener {
            Toast.makeText(this, "asdasdasd", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, CreateTaskActivity::class.java))
        }

        for (i in 0..100) {
            mDataSet.add(Task(i, "$i", false))
        }

        recycler = getRecycler()
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler.itemAnimator = DefaultItemAnimator()

        refreshAdapterInfo()

        val itemTouchHelperCallback = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }// i do not use it

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        deletedPosition = viewHolder.adapterPosition
                        deletedItem = mDataSet[deletedPosition]

                        mDataSet.removeAt(deletedPosition)
                        taskAdapter.notifyDataSetChanged()

                        Snackbar.make(
                            viewHolder.itemView,
                            "${deletedItem.title} был удален",
                            Snackbar.LENGTH_LONG
                        ).setAction("ОТМЕНА") {
                            mDataSet.add(deletedPosition, deletedItem)
                            taskAdapter.notifyItemInserted(deletedPosition)
                        }.show()
                    }
                    ItemTouchHelper.RIGHT -> {
                        deletedPosition = viewHolder.adapterPosition
                        deletedItem = mDataSet[deletedPosition]

                        mDataSet.removeAt(deletedPosition)
                        taskAdapter.notifyDataSetChanged()

                        deletedItem.isDone = true

                        mDataSet.add(deletedPosition, deletedItem)
                        taskAdapter.notifyItemInserted(deletedPosition)

                        Snackbar.make(
                            viewHolder.itemView,
                            "${deletedItem.title} выполнен",
                            Snackbar.LENGTH_LONG
                        ).setAction("ОТМЕНА") {
                            deletedItem.isDone = false
                            taskAdapter.notifyDataSetChanged()
                        }.show()
                    }
                }
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
                val itemView = viewHolder.itemView

                deleteIcon = ContextCompat.getDrawable(baseContext, R.drawable.ic_delete_30)!!
                doneIcon = ContextCompat.getDrawable(baseContext, R.drawable.ic_done_30)!!

                val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2

                if (dX > 0) {
                    //left
                    swipeBackgroundDelete.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                    doneIcon.setBounds(itemView.left + iconMargin, itemView.top + iconMargin,
                        itemView.left + iconMargin + doneIcon.intrinsicWidth, itemView.bottom - iconMargin)

                    swipeBackgroundDelete.draw(c)
                    doneIcon.draw(c)
                } else {
                    //right
                    swipeBackgroundDelete.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                    deleteIcon.setBounds(itemView.right - iconMargin - deleteIcon.intrinsicWidth, itemView.top + iconMargin,
                        itemView.right - iconMargin, itemView.bottom - iconMargin)

                    swipeBackgroundDelete.draw(c)
                    deleteIcon.draw(c)
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recycler)
    }

    fun refreshAdapterInfo() {
        taskAdapter = TaskAdapter(mDataSet)
        recycler.adapter = taskAdapter
    }

    public fun addToDataSet(task: Task) {
        mDataSet.add(task)
        refreshAdapterInfo()
    }
}