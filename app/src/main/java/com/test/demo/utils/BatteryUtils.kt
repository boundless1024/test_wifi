package com.test.demo.utils

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import com.test.demo.bean.BatteryItem

/**
 * 说明：获取手机电池相关信息
 * 文件名称：BatteryUtils
 * 创建者: hallo
 * 邮箱: hallo@xxx.xx
 * 时间: 2021/3/9 3:14 PM
 * 版本：V1.0.0
 */
object BatteryUtils {

    /***
     * 通过电量变化广播来获取电池温度信息，获取到的数值除以10 为需要的数据
     * @param context:context对象
     */
    fun getBatteryTemp(context: Context): Int {
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = context.registerReceiver(null, filter)

        getBatteryInfo(context)
        return (batteryStatus?.getIntExtra(
            BatteryManager.EXTRA_TEMPERATURE,
            0
        ) ?: 0) / 10
    }


    fun getBatteryInfo(context: Context): BatteryItem {
        var batteryItem: BatteryItem
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = context.registerReceiver(null, filter)
        val bundle = batteryStatus?.extras

        val level = bundle?.getInt(BatteryManager.EXTRA_LEVEL, -1)
        val scale = bundle?.getInt(BatteryManager.EXTRA_SCALE, -1)
        var levelStr = level.toString()
        if (level != -1 && scale != -1) {
            levelStr = ((level!! * 100.0 / scale!!).toInt()).toString() + "%"
        }
        val health = bundle?.getInt(BatteryManager.EXTRA_HEALTH, -1)
        val healthStr = getBatteryHealthStatus(health);
        val technology = bundle?.getString(BatteryManager.EXTRA_TECHNOLOGY, "")
        val voltage = bundle?.getInt(BatteryManager.EXTRA_VOLTAGE, -1)
        var voltageStr = voltage.toString()
        if (voltage > 0) {
            voltageStr = "${voltage / 1000.0}V"
        }
        val capacity = getBatteryCapacity(context)
        var capacityStr = capacity.toString()
        if (capacity != -1.0) {
            capacityStr = "${capacity}mAh"
        }

        var temp = bundle?.getInt(BatteryManager.EXTRA_TEMPERATURE, 0)
        if (temp > 0) {
            temp /= 10
        }
        var tempStr = "$temp°C"

        batteryItem = BatteryItem(tempStr, levelStr, capacityStr, technology, healthStr, voltageStr)
//        println("batteryItem = $batteryItem")
        return batteryItem;
    }

    /***
     * 获取电池容量
     */
    private fun getBatteryCapacity(context: Context): Double {
        var capacity = -1.0
        try {
            val powerProfile = Class.forName("com.android.internal.os.PowerProfile")
                .getConstructor(Context::class.java).newInstance(context)

            capacity = Class.forName("com.android.internal.os.PowerProfile")
                .getMethod("getAveragePower", String::class.java)
                .invoke(powerProfile, "battery.capacity") as Double
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return capacity
    }


    /**
     * 获取电池状态
     */
    private fun getBatteryHealthStatus(healthInt: Int): String {
        return when (healthInt) {
            BatteryManager.BATTERY_HEALTH_COLD -> "电池温度过低"
            BatteryManager.BATTERY_HEALTH_GOOD -> "健康"
            BatteryManager.BATTERY_HEALTH_DEAD -> "坏了"
            BatteryManager.BATTERY_HEALTH_OVERHEAT -> "电池温度过高"
            BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "电池电压过高"
            BatteryManager.BATTERY_HEALTH_UNKNOWN -> "未知"
            BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> "未知问题"
            else -> "未知"
        }
    }

}