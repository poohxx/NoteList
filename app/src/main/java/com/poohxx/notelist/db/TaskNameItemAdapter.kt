package com.poohxx.notelist.db

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.poohxx.notelist.R
import com.poohxx.notelist.databinding.NoteListItemBinding
import com.poohxx.notelist.databinding.TaskListItemBinding
import com.poohxx.notelist.entities.NoteItem
import com.poohxx.notelist.entities.TaskListItem

class TaskListItemAdapter(private val listener: Listener) :
    ListAdapter<TaskListItem, TaskListItemAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return if (viewType == 0) ItemHolder.createTaskItem(parent)
        else ItemHolder.createLibraryItem(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        if (getItem(position).itemType == 0) {
            holder.setItemData(getItem(position), listener)
        } else {
            holder.setLibraryData(getItem(position), listener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).itemType
    }

    class ItemHolder(val view: View) : RecyclerView.ViewHolder(view) {


        fun setItemData(taskListItem: TaskListItem, listener: Listener) {
            val binding = TaskListItemBinding.bind(view)
            binding.apply {
                tvName.text = taskListItem.name
                tvInfo.text = taskListItem.iteminfo
                tvInfo.visibility = infoVisibility(taskListItem)
                chBox.setOnClickListener{
                    setPaintFlagAndColor(binding)
                }
            }

        }

        fun infoVisibility(taskListItem: TaskListItem): Int {
            return if (taskListItem.iteminfo.isNullOrEmpty()) {
                View.GONE

            } else {
                View.VISIBLE
            }
        }

        private fun setPaintFlagAndColor(binding: TaskListItemBinding) {
            binding.apply {
                if (chBox.isChecked) {
                    tvName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    tvInfo.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    tvName.setTextColor(ContextCompat.getColor(binding.root.context,R.color.grey_light))
                    tvInfo.setTextColor(ContextCompat.getColor(binding.root.context,R.color.grey_light))
                } else {

                    tvName.paintFlags = Paint.ANTI_ALIAS_FLAG
                    tvInfo.paintFlags = Paint.ANTI_ALIAS_FLAG
                }
            }

        }

        fun setLibraryData(taskListItem: TaskListItem, listener: Listener) {
        }

        companion object {
            fun createTaskItem(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.task_list_item, parent, false)
                )
            }

            fun createLibraryItem(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.task_library_list_item, parent, false)
                )
            }
        }
    }

    class ItemComparator : DiffUtil.ItemCallback<TaskListItem>() {
        override fun areItemsTheSame(oldItem: TaskListItem, newItem: TaskListItem): Boolean {
            return oldItem.id == newItem.id
        }


        override fun areContentsTheSame(oldItem: TaskListItem, newItem: TaskListItem): Boolean {
            return oldItem == newItem
        }
    }

    interface Listener {
        fun deleteItem(id: Int)
        fun onClickItem(note: NoteItem)
    }

}