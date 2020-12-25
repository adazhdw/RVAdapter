package com.adazhdw.adapter.list.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding


/**
 * daguozhu
 * create at 2020/4/2 15:53
 * description:
 */
abstract class ViewBindingFragment : CoroutinesFragment(), IFragment {

    final override val layoutId: Int
        get() = 0

    /**
     * 是否初始化过布局
     */
    protected var isViewInitiated = false

    /**
     * 是否加载过数据
     */
    protected var isDataInitiated = false

    protected lateinit var mContext: Context
    lateinit var mActivity: FragmentActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        mActivity = context as FragmentActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return initViewBinding(inflater, container).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewInitiated = true
        initView(view)
        initData()
        prepareRequest()
    }

    private fun prepareRequest() {
        if (isViewInitiated && !isDataInitiated && !isHidden) {
            requestStart()
            isDataInitiated = true
        }
    }

    override fun needEventBus(): Boolean = false

    abstract fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ViewBinding
}