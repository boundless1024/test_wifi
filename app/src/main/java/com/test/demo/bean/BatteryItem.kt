package com.test.demo.bean

/**
 * 说明：电池相关信息item
 * 文件名称：BatteryItem
 * 创建者: hallo
 * 邮箱: hallo@xxx.xx
 * 时间: 2021/3/9 5:00 PM
 * 版本：V1.0.0
 */
data class BatteryItem(
    val temp: String,
    val level:String,
    val capacity:String,
    val technology:String,
    val health:String,
    val voltage: String
)
