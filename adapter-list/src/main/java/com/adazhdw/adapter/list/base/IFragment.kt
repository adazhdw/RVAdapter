package com.adazhdw.adapter.list.base

import android.view.View

/**
 * Created by daguozhu
 * at: 2020/3/5 15:44.
 * desc:
 */
interface IFragment {

    /**
     * 返回布局Id
     */
    val layoutId: Int

    /**
     * 初始化数据
     */
    fun initData(){}

    /**
     * 初始化View
     */
    fun initView(view: View){}

    /**
     * 是否需要EventBus
     */
    fun needEventBus(): Boolean = false

    /**
     * 网络请求开始
     */
    fun requestStart(){}

}