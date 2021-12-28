package com.poohxx.notelist.activities

import android.app.Application
import com.poohxx.notelist.db.MainDataBase

class MainApp : Application() {
    val dataBase by lazy {
        MainDataBase.getDataBase(this)
    }
}