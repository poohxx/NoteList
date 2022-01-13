package com.poohxx.notelist.db

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.poohxx.notelist.R
import com.poohxx.notelist.databinding.NoteListItemBinding
import com.poohxx.notelist.entities.NoteItem
import com.poohxx.notelist.entities.TaskListItem
import com.poohxx.notelist.utils.HtmlManager

class TaskListItemAdapter(private val listener: Listener) :
    ListAdapter<TaskListItem, TaskListItemAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return if(viewType == 0) ItemHolder.createTaskItem(parent)
        else ItemHolder.createLibraryItem(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        if (getItem(position).itemType == 0) {
            holder.setItemData(getItem(position), listener)
        } else{
            holder.setLibraryData(getItem(position),listener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).itemType
    }

    class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = NoteListItemBinding.bind(view)

        fun setItemData(taskListItem: TaskListItem, listener: Listener) = with(binding) {
            }

        fun setLibraryData(taskListItem: TaskListItem, listener: Listener) = with(binding) {
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
                        .inflate(R.layout.note_list_item, parent, false)
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