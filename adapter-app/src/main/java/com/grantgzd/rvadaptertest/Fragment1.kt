package com.grantgzd.rvadaptertest

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewbinding.ViewBinding
import com.adazhdw.adapter.core.DefaultItem
import com.adazhdw.adapter.core.bind
import com.adazhdw.adapter.core.listAdapter
import com.adazhdw.adapter.list.base.ViewBindingFragment
import com.adazhdw.adapter.list.ext.stopRefresh
import com.adazhdw.adapter.loadmore.defaultLoadMoreListener
import com.adazhdw.adapter.loadmore.loadMoreAdapter
import com.grantgzd.rvadaptertest.databinding.LayoutViewPagerItem1Binding

/**
 * FileName: Fragment1
 * Author: adazhdw
 * Date: 2020/12/25 11:11
 * Description:
 * History:
 */
class Fragment1 : ViewBindingFragment() {

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var pagerItem1Binding: LayoutViewPagerItem1Binding
    override fun initViewBinding(inflater: LayoutInflater, container: ViewGroup?): ViewBinding {
        pagerItem1Binding = LayoutViewPagerItem1Binding.inflate(inflater, container, false)
        return pagerItem1Binding
    }


    override fun initView(view: View) {
        val listAdapter = listAdapter {
            addData(homeModel(HomeModel("msg----")))
        }
        val loadMoreAdapter = loadMoreAdapter(listAdapter)

        loadMoreAdapter.bind(pagerItem1Binding.recyclerview, GridLayoutManager(context, 2))
        pagerItem1Binding.add.setOnClickListener {
            listAdapter.addData(homeBindModel(HomeModel("msg----")))
            listAdapter.scrollToBottom()
        }
        val list = mutableListOf<DefaultItem<HomeModel>>().apply {
            for (i in 0 until 5) {
                add(homeModel(HomeModel("msg----")))
            }
        }
        listAdapter.addData(list)

        pagerItem1Binding.swipe.setOnRefreshListener {
            handler.postDelayed({
                listAdapter.setData(list)
                pagerItem1Binding.swipe.stopRefresh()
            }, 1000)
        }

        //加载更多
        pagerItem1Binding.recyclerview.defaultLoadMoreListener {
            pagerItem1Binding.recyclerview.postDelayed({
                listAdapter.addData(list)
                loadMoreAdapter.loadComplete(hasMore = listAdapter.getData().size < 20)
            }, 1000)
        }
    }
}