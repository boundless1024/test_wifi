package com.test.demo.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseVBFragment<T : ViewDataBinding> : BaseFragment() {
    protected lateinit var mDataBinding: T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return mDataBinding.root
    }
}