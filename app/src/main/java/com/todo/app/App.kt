package com.todo.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger

/**
 * Created by ingizly on 2019-09-24
 **/
class App: Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: Context? = null
        fun applicationContext(): Context? {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = applicationContext
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)
    }
}