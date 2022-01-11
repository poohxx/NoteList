package com.poohxx.notelist.db

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.poohxx.notelist.R
import com.poohxx.notelist.databinding.NameListItemBinding
import com.poohxx.notelist.databinding.NoteListItemBinding
import com.poohxx.notelist.entities.NoteItem

import com.poohxx.notelist.entities.TaskListNames
import com.poohxx.notelist.utils.HtmlManager
import com.poohxx.notelist.utils.MyTouchListener

class TaskNameAdapter(private val listener: Listener) :
    ListAdapter<TaskListNames, TaskNameAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position),listener)
    }

    class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = NameListItemBinding.bind(view)

        fun setData(taskListNameItem: TaskListNames, listener: Listener) = with(binding) {
            tvListName.text = taskListNameItem.name
            tvTimeListName.text = taskListNameItem.time
            itemView.setOnClickListener {
            }
            imBtnDelete.setOnClickListener {
                listener.deleteItem(taskListNameItem.id!!)

            }
            imBtnEdit.setOnClickListener {
                listener.onEditItem(taskListNameItem)

            }
        }

        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.name_list_item, parent, false)
                )
            }
        }
    }

    class ItemComparator : DiffUtil.ItemCallback<TaskListNames>() {
        override fun areItemsTheSame(oldItem: TaskListNames, newItem: TaskListNames): Boolean {
            return oldItem.id == newItem.id
        }


        override fun areContentsTheSame(oldItem: TaskListNames, newItem: TaskListNames): Boolean {
            return oldItem == newItem
        }
    }

    interface Listener {
        fun deleteItem(id: Int)
        fun onClickItem(taskListName: TaskListNames)
        fun onEditItem(taskListName: TaskListNames)
    }

}