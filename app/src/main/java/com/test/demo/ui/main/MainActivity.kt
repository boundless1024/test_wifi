package com.test.demo.ui.main

import android.os.Bundle
import com.test.demo.R
import com.test.demo.base.BaseVBActivity
import com.test.demo.databinding.MainActivityBinding

class MainActivity : BaseVBActivity<MainActivityBinding>() {

    override fun afterCreate(savedInstanceState: Bundle?) {
        super.afterCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    override fun getLayoutId(): Int = R.layout.main_activity
}