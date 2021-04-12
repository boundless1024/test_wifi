package com.test.demo.base

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.test.demo.base.BaseActivity

/**
 *  说明：//TODO
 *  文件名称：BaseVBActivity
 *  创建者: hallo
 *  邮箱: hallo@xxx.xx
 *  时间: 2019/11/22 17:16
 *  版本：V1.0.1
 */
abstract class BaseVBActivity< T : ViewDataBinding> : BaseActivity() {
    lateinit var mDataBinding: T
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView<T>(this, getLayoutId())
        afterCreate(savedInstanceState)
    }
}