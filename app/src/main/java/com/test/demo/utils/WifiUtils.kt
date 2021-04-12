@file:Suppress("DEPRECATION")

package com.test.demo.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.provider.Settings
import android.text.TextUtils
import com.test.demo.bean.WifiItem

/**
 * 说明：wifi工具
 * 文件名称：WifiUtils.kt
 * 创建者: hallo
 * 邮箱: hallo@xxx.xx
 * 时间: 2021/4/10 1:53 PM
 * 版本：V1.0.0
 */
object WifiUtils {

    /***
     * 判断wifi网络是否可用
     * - wifi已开启
     * - WiFi已经连接
     */
    fun isWifiNetworkAvailable(context: Context): Boolean {
        val wifiManager = getWifiManager(context)
        if (wifiManager.isWifiEnabled) {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)

            return networkInfo?.isConnected == true
        }

        return false
    }

    /***
     * 获取附近开放的扫描的wifi列表
     * 备注：需要在{@link WifiManager#SCAN_RESULTS_AVAILABLE_ACTION}广播之后获取
     */
    fun getLocationWifiList(context: Context): MutableList<WifiItem> {

        var wifiList = mutableListOf<WifiItem>()
        val wifiManager = getWifiManager(context)
        val resultList = wifiManager.scanResults



        resultList.sortWith(Comparator { o1, o2 -> o2.level.compareTo(o1.level) }) //根据WiFi信号强度排序

        resultList.forEach {
            val ssid = it.SSID
            val level = WifiManager.calculateSignalLevel(it.level, 4)
            if (!TextUtils.isEmpty(ssid)) {  //去除wifi名称为空的热点
                val wifiItem = WifiItem(ssid, level)
                if (!wifiList.contains(wifiItem)) {
                    wifiList.add(wifiItem)

                    println("wifiItem = $wifiItem")
                }
            }
        }
        return wifiList
    }

    private fun getWifiManager(context: Context): WifiManager {
        return context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    private fun genWifiConfiguration(ssid: String, pws: String): WifiConfiguration {
        val wifiConfiguration = WifiConfiguration()
        wifiConfiguration.allowedAuthAlgorithms.clear()
        wifiConfiguration.allowedGroupCiphers.clear()
        wifiConfiguration.allowedKeyManagement.clear()
        wifiConfiguration.allowedPairwiseCiphers.clear()
        wifiConfiguration.allowedProtocols.clear()

        wifiConfiguration.SSID = "\"" + ssid + "\"";

        wifiConfiguration.preSharedKey = "\"" + pws + "\""
        wifiConfiguration.hiddenSSID = true
        wifiConfiguration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN)
        wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP)
        wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK)
        wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP)
        wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP)
        wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP)
        wifiConfiguration.status = WifiConfiguration.Status.ENABLED

        return wifiConfiguration
    }

    /**
     * 连接wifi
     * @param ssid:wifi名称
     * @param pwd:wifi密码
     */
    fun connectWifiWithPwd(context: Context, ssid: String, pwd: String) {
        var wifiManager = getWifiManager(context)
        var networkId = wifiManager.addNetwork(genWifiConfiguration(ssid, pwd))
        wifiManager.enableNetwork(networkId, true)

    }

    @SuppressLint("MissingPermission")
    fun getWifiConfig(context: Context, ssid: String): WifiConfiguration? {
        var wifiManager = getWifiManager(context)
        val list = wifiManager.configuredNetworks
        if (list.isNotEmpty() && !TextUtils.isEmpty(ssid)) {
            return list.firstOrNull { "\""+ssid+"\"" == it.SSID } //注意返回的it.SSID
        }
        return null
    }

    /***
     * 判断给定的wifi名称是否信息已经保存
     */
    fun isWifiSaved(context: Context, ssid: String): Boolean {
        return getWifiConfig(context, ssid) != null
    }

    /***
     * 取消保存的wifi连接信息
     */
    fun forgetWifi(context: Context, ssid: String) {
        val wifiCfg = getWifiConfig(context, ssid)
        if (null != wifiCfg) {
            getWifiManager(context).removeNetwork(wifiCfg.networkId)
        }
    }

    /***
     * 扫描附近wifi
     * 备注：wifi管理器扫描附近wifi列表后，wifi管理器会在扫描结束后
     * 发送一个广播action {@link SCAN_RESULTS_AVAILABLE_ACTION }
     * 参考方法{@link getLocationWifiList()}
     */
    fun scanLocationWifi(context: Context) {
        getWifiManager(context).startScan()
    }


    /**
     * 跳转到系统wifi设置界面
     */
    fun goToWifiSettings(context: Context) {
        context.startActivity(
            Intent(Settings.ACTION_WIFI_SETTINGS)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    /***
     * 直接打开wifi
     * 备注：如果targetSdkVersion 大于 28（P-9.0系统）即 29（Q-10.0系统）以及以上,此方法无效
     */
    fun openWifi(context: Context) {
        getWifiManager(context).isWifiEnabled = true
    }


    /**
     * 获取wifi的名称
     * @param context
     * @return
     */
    fun getWiFiName(context: Context): String? {
        var wifiName = ""
        val wm = getWifiManager(context)
        val wifiInfo = wm.connectionInfo
        if (wifiInfo != null) {
            val s = wifiInfo.ssid
            if (s.length > 2 && s[0] == '"' && s[s.length - 1] == '"') {
                wifiName = s.substring(1, s.length - 1)
            }
        }
        return wifiName
    }

}