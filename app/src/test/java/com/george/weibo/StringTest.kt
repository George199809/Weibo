package com.george.weibo

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class StringTest {

    @Test
    fun picTransaction(){
        val pics = listOf<String>("http://wx1.sinaimg.cn/thumbnail/005N1R93gy1gttynj2gnpj60qq0zmgqf02.jpg",
        "http://wx4.sinaimg.cn/thumbnail/005N1R93gy1gttynixinjj60qq0zmdjx02.jpg",
        "http://wx1.sinaimg.cn/thumbnail/005N1R93gy1gttynj0ouxj60qq0zm42k02.jpg",
        "http://wx1.sinaimg.cn/thumbnail/005N1R93gy1gttyniwp3tj60qq0zmn0q02.jpg")
        val thumbnail_pic = "http://wx1.sinaimg.cn/thumbnail/005N1R93gy1gttynj2gnpj60qq0zmgqf02.jpg"
        val bmiddle_pic = "http://wx1.sinaimg.cn/bmiddle/005N1R93gy1gttynj2gnpj60qq0zmgqf02.jpg"
        val original_pic = "http://wx1.sinaimg.cn/large/005N1R93gy1gttynj2gnpj60qq0zmgqf02.jpg"

        val bmiddleHeader = getUrlHeader(bmiddle_pic)
        val tmp = bmiddleHeader+getPicId(pics[1])
        println(tmp)
    }


    fun getPicId(url: String) : String{
        var index = url.length-1
        while(index > 0){
            if(url[index] == '/')
                break;
            index--
        }

        return url.substring(index+1, url.length)
    }

    fun getUrlHeader(url : String) : String{
        var index = url.length-1
        while(index > 0){
            if(url[index] == '/')
                break;
            index--
        }

        return url.substring(0, index+1)
    }

}