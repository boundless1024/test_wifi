package com.test.demo.ui.wifi

import android.Manifest
import android.app.AlertDialog
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.*
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.test.demo.R
import com.test.demo.base.BaseVBActivity
import com.test.demo.bean.WifiItem
import com.test.demo.databinding.WifiActivityBinding
import com.test.demo.utils.WifiUtils


/**
 * 说明：查看附近wifi列表，连接wifi
 * 文件名称：WifiActivity.kt
 * 创建者: hallo
 * 邮箱: hallo@xxx.xx
 * 时间: 2021/4/10 10:26 AM
 * 版本：V1.0.0
 */
@Suppress("DEPRECATION")
class WifiActivity : BaseVBActivity<WifiActivityBinding>() {

    companion object {
        const val REQUEST_LOCATION_CODE = 0x34
        fun actionWifiTest(context: Context) {
            val intent = Intent(context, WifiActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var mWifiManager: WifiManager

    private lateinit var mConnectivityManager: ConnectivityManager


    private val mBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action != null) {
                when (action) {
                    WifiManager.NETWORK_STATE_CHANGED_ACTION -> {
                        val networkInfo =
                            intent.getParcelableExtra<NetworkInfo>(WifiManager.EXTRA_NETWORK_INFO)
                        println("networkInfo = $networkInfo\n\n\n\n")
                        scanLocationWifiList()
                    }
                    WifiManager.SCAN_RESULTS_AVAILABLE_ACTION -> {
                        var wifiList = WifiUtils.getLocationWifiList(mActivity)
                        mDataBinding.recyclerView.adapter =
                            WifiListAdapter(wifiList, application)
                    }

                    WifiManager.SUPPLICANT_STATE_CHANGED_ACTION -> {
                        val error = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, -100)
                        println("error = $error")
                        if (error == WifiManager.ERROR_AUTHENTICATING) { //密码不正确
                            hintPwdError()
                        }
                    }
                }
            }
        }
    }

    /***
     * 监听网络发生变化
     */
    private val mNetworkCallback: ConnectivityManager.NetworkCallback =
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) { //监听的网络类型可以使用
                super.onAvailable(network)
                println("NetworkCallback onAvailable network = $network ")
            }

            override fun onCapabilitiesChanged( //监听的网络类型可以使用之后就会执行
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                println("NetworkCallback onCapabilitiesChanged network = $network ")
            }

            override fun onUnavailable() { //网络不可用
                super.onUnavailable()
                println("NetworkCallback onUnavailable ")
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                println("NetworkCallback onLost network = $network ")
            }

        }

    /***
     * 提示密码不正确
     */
    private fun hintPwdError() {
        Toast.makeText(mActivity, "wifi密码错误", Toast.LENGTH_SHORT).show()

    }

    override fun afterCreate(savedInstanceState: Bundle?) {
        super.afterCreate(savedInstanceState)

        mDataBinding.recyclerView.layoutManager = LinearLayoutManager(mActivity)

        mWifiManager = application.getSystemService(Context.WIFI_SERVICE) as WifiManager

        mConnectivityManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


        mDataBinding.btnOpen.visibility = View.GONE
        mDataBinding.btnOpen.setOnClickListener {
            scanLocationWifiList()
        }

        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build()
        mConnectivityManager.registerNetworkCallback(request, mNetworkCallback)


        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION) // 监听网络状态发生变化
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) // 附近wifi列表可以获取到
        intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION) //wifi连接失败，例如密码不对
        registerReceiver(mBroadcastReceiver, intentFilter)

    }

    /***
     * 扫描附近可用的wifi列表，备注需要定位权限
     */
    private fun scanLocationWifiList() {
        val isWifiEnable = WifiUtils.isWifiNetworkAvailable(mActivity)
        val hasLocationPermission =
            PackageManager.PERMISSION_GRANTED == checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        if (hasLocationPermission) {
            WifiUtils.scanLocationWifi(mActivity)
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_CODE
            )
        }
        println("wifi是否已经连接 = $isWifiEnable 是否有定位权限 = $hasLocationPermission")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mDataBinding.btnOpen.visibility = View.GONE
                WifiUtils.scanLocationWifi(mActivity)
            }
            else{
                Toast.makeText(mActivity, "需要开启定位权限，查看附近WIFI列表", Toast.LENGTH_LONG).show()
                mDataBinding.btnOpen.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mBroadcastReceiver)
        if (null != mConnectivityManager) {
            mConnectivityManager.unregisterNetworkCallback(mNetworkCallback)
        }
    }


    class WifiListAdapter(
        private val itemList: List<WifiItem>, private val application: Application
    ) : RecyclerView.Adapter<WifiListAdapter.VH>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val childView = LayoutInflater.from(parent.context).inflate(R.layout.item_wifi, null)
            childView.layoutParams =
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            return VH(childView)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val ssid = itemList[position].ssid
            val isSaved = WifiUtils.isWifiSaved(application, ssid)
            holder.tvName.text = ssid
            holder.tvStatus.text = "" + isSaved
            holder.itemView.setOnClickListener {
                if (isSaved) {
                    AlertDialog.Builder(holder.itemView.context)
                        .setTitle("提示")
                        .setMessage("是否取消保存的wifi信息")
                        .setPositiveButton("确定") { _, _ -> WifiUtils.forgetWifi(application, ssid) }
                        .setNegativeButton("取消") { _, _ -> {} }
                        .create().show()
                } else {
                    val editView = EditText(holder.itemView.context)
                    editView.hint = "请输入选择的wifi密码"
                    AlertDialog.Builder(holder.itemView.context)
                        .setTitle("提示")
                        .setView(editView)
                        .setPositiveButton("确定") { _, _ ->

                            if (!TextUtils.isEmpty(editView.text)) {
                                WifiUtils.connectWifiWithPwd(
                                    application,
                                    ssid,
                                    editView.text.toString()
                                )
                            }
                        }
                        .setNegativeButton("取消") { _, _ -> {} }
                        .create().show()

                }
            }
        }

        override fun getItemCount(): Int {
            return itemList.size
        }

        class VH(itemView: View) : ViewHolder(itemView) {
            var tvName: TextView = itemView.findViewById(R.id.name)
            var tvStatus: TextView = itemView.findViewById(R.id.tv_status)

        }
    }

    override fun getLayoutId(): Int = R.layout.wifi_activity
}