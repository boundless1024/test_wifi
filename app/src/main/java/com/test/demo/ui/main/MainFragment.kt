package com.test.demo.ui.main

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.demo.R
import com.test.demo.base.BaseVBFragment
import com.test.demo.databinding.MainFragmentBinding
import com.test.demo.ui.wifi.WifiActivity
import com.test.demo.utils.*

class MainFragment : BaseVBFragment<MainFragmentBinding>() {

    companion object {
        fun newInstance() = MainFragment()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val countDownTimer: CountDownTimer = object : CountDownTimer(600000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                mDataBinding.tvCpu.text =
                    "CPU温度 ${millisUntilFinished}：" + CpuTempUtils.getTemp() + "°C"

                val batteryItem = BatteryUtils.getBatteryInfo(mActivity);
                mDataBinding.tvBattery.text =
                    "温度：${batteryItem.temp}\n" +
                            "电量：${batteryItem.level}\n" +
                            "容量：${batteryItem.capacity}\n" +
                            "电压：${batteryItem.voltage}\n" +
                            "状态：${batteryItem.health}"


                mDataBinding.tvMemory.text =
                    "可用内存：${MemoryUtils.getAvailMemWithUnit(mActivity)}\n" +
                            "总共内存：${MemoryUtils.getTotalMemWithUnit(mActivity)}\n" +
                            "占百分比：${MemoryUtils.getMemPercent(mActivity)}"


                mDataBinding.tvStorage.text =
                    "可用存储：${PhoneStorageUtils.getAvailableStorageWithUnit(mActivity)}\n" +
                            "总共大小：${PhoneStorageUtils.getTotalStorageWithUnit(mActivity)}\n" +
                            "可用比例：${PhoneStorageUtils.getStoragePercent(mActivity)}"

                mDataBinding.rvRecyclerView.run {
                    layoutManager = LinearLayoutManager(mActivity)
                    adapter = RunningAppsAdapter(AppUtils.getRunningAppList(mActivity))
                }
            }

            override fun onFinish() {
            }

        }
        countDownTimer.start()

        mDataBinding.tvKill.setOnClickListener {
            val activityManager =
                mActivity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            AppUtils.getRunningAppList(mActivity).forEach {
                activityManager.killBackgroundProcesses(it.pkg)
            }
        }

        mDataBinding.tvWifi.setOnClickListener{
            WifiActivity.actionWifiTest(mActivity)
        }
    }


    override fun getLayoutId(): Int = R.layout.main_fragment
}
