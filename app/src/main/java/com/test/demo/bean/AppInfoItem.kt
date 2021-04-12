package com.test.demo.bean

import android.graphics.drawable.Drawable

/**
 * 说明：//TODO
 * 文件名称：AppInfoItem
 * 创建者: hallo
 * 邮箱: hallo@xxx.xx
 * 时间: 2021/3/10 2:03 PM
 * 版本：V1.0.0
 */
data class AppInfoItem(
    val appName: String,
    val icon: Drawable,
    val pkg: String,
    val memSize:String

) {
    override fun equals(other: Any?): Boolean {
        return other is AppInfoItem && appName == other.appName
    }
}


