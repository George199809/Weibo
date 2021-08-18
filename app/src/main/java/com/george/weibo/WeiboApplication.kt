package com.george.weibo

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class WeiboApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context : Context

        const val TOKEN="2.001llhEG0wOrUP78781625eb0AOqmy"
//        const val TOKEN="2.00W1O8PIOU6bVBc6a5a31cd80sBcWv"

        const val UID = "5566334042"

    }


    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}