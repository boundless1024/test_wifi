package com.test.demo.utils

import java.io.BufferedReader
import java.io.File
import java.io.FileReader


/**
 * 说明：获取cpu温度
 * 文件名称：CpuTempUtils
 * 创建者: hallo
 * 邮箱: hallo@xxx.xx
 * 时间: 2021/3/9 12:16 PM
 * 版本：V1.0.0
 */
object CpuTempUtils {

    private val FILE_LIST = arrayOf(
        "/sys/devices/system/cpu/cpu0/cpufreq/cpu_temp",
        "/sys/devices/system/cpu/cpu0/cpufreq/FakeShmoo_cpu_temp",
        "/sys/devices/platform/tegra-i2c.3/i2c-4/4-004c/temperature",
        "/sys/devices/platform/omap/omap_temp_sensor.0/temperature",
        "/sys/devices/platform/tegra_tmon/temp1_input",
        "/sys/devices/platform/s5p-tmu/temperature",
        "/sys/devices/platform/s5p-tmu/curr_temp",
        "/sys/devices/virtual/thermal/thermal_zone1/temp",
        "/sys/devices/virtual/thermal/thermal_zone0/temp",
        "/sys/class/thermal/thermal_zone0/temp",
        "/sys/class/thermal/thermal_zone1/temp",
        "/sys/class/thermal/thermal_zone3/temp",
        "/sys/class/thermal/thermal_zone4/temp",
        "/sys/class/hwmon/hwmon0/device/temp1_input",
        "/sys/class/i2c-adapter/i2c-4/4-004c/temperature",
        "/sys/kernel/debug/tegra_thermal/temp_tj",
        "/sys/htc/cpu_temp",
        "/sys/devices/platform/tegra-i2c.3/i2c-4/4-004c/ext_temperature",
        "/sys/devices/platform/tegra-tsensor/tsensor_temperature"
    )

    fun getTemp(): Int {
        var temp = 0
        for (file in FILE_LIST) {
            val file2 = File(file)
            if (file2.exists()) {
                try {
                    val fileReader = FileReader(file2)
                    val bufferedReader = BufferedReader(fileReader)
                    val readLine: String = bufferedReader.readLine()
                    bufferedReader.close()
                    fileReader.close()
                    temp = handleOriginTemp(readLine.toDouble())
                } catch (e2: Exception) {
                    e2.printStackTrace()
                }
            }
        }
        return temp
    }

    /***
     * 从文件读取到的温度 除以 1000 为需要的cpu温度
     */
    private fun handleOriginTemp(temp: Double): Int {
        if (isTempValid(temp)) {
            return temp.toInt()
        }
        return (temp * 1.0 / 1000).toInt();
    }

    /***
     * 是否是合理的温度范围
     * 【-30 到 200 】
     */
    private fun isTempValid(temp: Double): Boolean = temp in -30.0..200.0

}