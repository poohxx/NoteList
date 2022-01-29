package com.poohxx.notelist.dialogs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.poohxx.notelist.databinding.EditListItemDialogBinding
import com.poohxx.notelist.entities.TaskListItem

object EditListItemDialog {
    fun showDialog(context: Context, item: TaskListItem, listener: Listener) {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        val binding = EditListItemDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        binding.apply {
            edNameItem.setText(item.name)
            editInfoItem.setText(item.itemInfo)
            if(item.itemType==1)editInfoItem.visibility= View.GONE

            btnUpdateItem.setOnClickListener {
                if (edNameItem.text.toString().isNotEmpty()) {
                    listener.onClick(
                        item.copy(
                            name = edNameItem.text.toString(),
                            itemInfo = editInfoItem.text.toString()
                        )
                    )
                }
                dialog?.dismiss()
            }
        }




        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(null)
        dialog.show()
    }

    interface Listener {
        fun onClick(item: TaskListItem)
    }
}