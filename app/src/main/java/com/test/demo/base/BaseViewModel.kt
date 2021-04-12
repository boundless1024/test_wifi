package com.test.demo.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel< T> :ViewModel()
{
    val mUiModel = MutableLiveData<BaseUiModel<T>>()
}