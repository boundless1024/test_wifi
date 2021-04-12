package com.test.demo.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    protected lateinit var mFragment: BaseFragment
    protected lateinit var mActivity: BaseActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as BaseActivity
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFragment = this
    }

    abstract fun getLayoutId(): Int
}