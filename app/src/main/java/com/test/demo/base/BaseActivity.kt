package com.test.demo.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 *  说明：//TODO
 *  文件名称：BaseActivity
 *  创建者: hallo
 *  邮箱: hallo@xxx.xx
 *  时间: 2019/11/22 17:10
 *  版本：V1.0.1
 */
abstract class BaseActivity : AppCompatActivity() {
    protected lateinit var mActivity: BaseActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = this
    }

    abstract fun getLayoutId(): Int

    open fun afterCreate(savedInstanceState: Bundle?) {

    }
}