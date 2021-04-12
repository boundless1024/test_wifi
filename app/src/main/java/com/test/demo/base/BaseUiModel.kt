package com.test.demo.base

open class BaseUiModel<out T> (val apiData:T?=null,val success: Boolean,val errorMsg:String?="请求失败")