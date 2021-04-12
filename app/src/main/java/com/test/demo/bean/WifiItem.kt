package com.test.demo.bean

/**
 * 说明：//TODO
 * 文件名称：WifiItem.kt
 * 创建者: hallo
 * 邮箱: hallo@xxx.xx
 * 时间: 2021/4/10 12:05 PM
 * 版本：V1.0.0
 */
data class WifiItem(val ssid: String, val level: Int) {

    override fun equals(other: Any?): Boolean {
        if (other is WifiItem) {
            return this.ssid?.equals(other?.ssid)
        }
        return super.equals(other)
    }

    override fun toString(): String {
        return "ssid = $ssid  level = $level"
    }
}
