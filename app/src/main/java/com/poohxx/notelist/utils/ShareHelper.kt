package com.poohxx.notelist.utils

import android.content.Intent
import com.poohxx.notelist.entities.TaskListItem

object ShareHelper {
    fun shareTaskList(taskList: List<TaskListItem>, listName:String): Intent{
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plane"
        intent.apply {
            putExtra(Intent.EXTRA_TEXT, makeShareText(taskList,listName))
        }
        return intent
    }
    private fun makeShareText(taskList: List<TaskListItem>, listName: String): String{
       val sBuilder = StringBuilder()
        sBuilder.append("<<$listName>>")
        sBuilder.append("\n")
        var counter = 0
        taskList.forEach{
            sBuilder.append("${++counter} - ${it.name} (${it.itemInfo})")
            sBuilder.append("\n")
        }
        return sBuilder.toString()
    }
}