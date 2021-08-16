package com.george.weibo.tools

import java.text.SimpleDateFormat
import java.util.*

class MyDate(val date : Date){
    val month : Int = SimpleDateFormat("MM").format(date).toInt()
    val day : Int = SimpleDateFormat("dd").format(date).toInt()
    val time : String = SimpleDateFormat("HH:mm").format(date)

    override fun toString(): String {
        return month.toString()+"月" +day.toString()+"号  " + time
    }
}