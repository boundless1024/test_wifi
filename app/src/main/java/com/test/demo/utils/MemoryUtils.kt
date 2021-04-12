package com.test.demo.utils

import android.app.ActivityManager
import android.content.Context
import android.text.format.Formatter

/**
 * 说明：获取设备RAM内存信息
 * 文件名称：MemoryUtils
 * 创建者: hallo
 * 邮箱: hallo@xxx.xx
 * 时间: 2021/3/9 6:48 PM
 * 版本：V1.0.0
 */
object MemoryUtils {
    fun getAvailMem(context: Context): Long {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        return memoryInfo.availMem
    }

    fun getTotalMem(context: Context): Long {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        return memoryInfo.totalMem
    }


    fun getAvailMemWithUnit(context: Context): String {
        val size = getAvailMem(context)
        return Formatter.formatFileSize(context, size)

    }


    fun getTotalMemWithUnit(context: Context): String {
        val size = getTotalMem(context)
        return Formatter.formatFileSize(context, size)
    }

    fun getMemPercent(context: Context): String {
        val totalSize = getTotalMem(context)
        val availSize = getAvailMem(context)

        return "${availSize * 100 / totalSize}%"
    }
}