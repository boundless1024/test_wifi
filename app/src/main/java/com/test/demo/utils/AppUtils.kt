package com.test.demo.utils

import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import com.test.demo.bean.AppInfoItem


/**
 * 说明：//TODO
 * 文件名称：AppUtils
 * 创建者: hallo
 * 邮箱: hallo@xxx.xx
 * 时间: 2021/3/9 7:36 PM
 * 版本：V1.0.0
 */
object AppUtils {


    fun getRunningAppList(context: Context): MutableList<AppInfoItem> {
        val mutableList = mutableListOf<AppInfoItem>()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val packageManager = context.packageManager

        activityManager.runningAppProcesses?.forEach {
            if (it.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                it.pkgList.forEach { pkgName ->
                    run {

                        val packageInfo = packageManager.getPackageInfo(pkgName, 0)


                        val isUserApp =
                            packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0

                        val appName =
                            packageManager.getApplicationLabel(packageInfo.applicationInfo)
                                .toString()
                        packageInfo.applicationInfo.loadLabel(packageManager).toString()
                        val appIcon = packageInfo.applicationInfo.loadIcon(packageManager)


                        var memoryInfo = activityManager.getProcessMemoryInfo(intArrayOf(it.pid))
                        val usedMemSize = memoryInfo[0].totalPss.toString()
                        if (isUserApp) {
                            val appInfoItem = AppInfoItem(appName, appIcon, pkgName, usedMemSize)
                            if (!mutableList.contains(appInfoItem)) {
                                mutableList.add(appInfoItem)
//                                println("appName = $appName  pkgName = $pkgName  isUserApp = $isUserApp usedMemSize =$usedMemSize")
                            }
                        }

                    }

                }
            }
        }

        return mutableList
    }

}