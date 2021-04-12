package com.test.demo.utils

import android.content.Context
import android.os.Environment
import android.text.format.Formatter
import java.io.File

/**
 * 说明：//TODO
 * 文件名称：PhoneStorageUtils
 * 创建者: hallo
 * 邮箱: hallo@xxx.xx
 * 时间: 2021/3/9 7:11 PM
 * 版本：V1.0.0
 */
object PhoneStorageUtils {

    private fun getTotalStorage(): Long {
        val path: File = Environment.getDataDirectory()
        return path.totalSpace
    }


    private fun getAvailableStorage(): Long {
        val path: File = Environment.getDataDirectory()
        return path.usableSpace
    }

    private fun getUsedStorage(): Long {
        return getTotalStorage() - getAvailableStorage()
    }


    fun getTotalStorageWithUnit(context: Context): String {
        return Formatter.formatFileSize(context, getTotalStorage())
    }


    fun getAvailableStorageWithUnit(context: Context): String {
        return Formatter.formatFileSize(context, getAvailableStorage())
    }

    fun getStoragePercent(context: Context): String {
        return "${getAvailableStorage() * 100 / getTotalStorage()}%";
    }

    fun getUsedStoragePercent(context: Context): String {
        return "${getUsedStorage() * 100 / getTotalStorage()}%";
    }


}