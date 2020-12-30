package com.grantgzd.rvadaptertest

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.adazhdw.adapter.core.DefaultItem
import com.adazhdw.adapter.list.ListFragment
import com.adazhdw.adapter.list.base.ViewBindingFragment
import com.adazhdw.adapter.loadmore.LoadMoreRecyclerView
import com.grantgzd.rvadaptertest.databinding.LayoutViewPagerItem2Binding

/**
 * FileName: Fragment1
 * Author: adazhdw
 * Date: 2020/12/25 11:11
 * Description:
 * History:
 */
class Fragment2 : ViewBindingFragment() {

    private lateinit var pagerItem1Binding: LayoutViewPagerItem2Binding
    override fun initViewBinding(inflater: LayoutInflater, container: ViewGroup?): ViewBinding {
        pagerItem1Binding = LayoutViewPagerItem2Binding.inflate(inflater, container, false)
        return pagerItem1Binding
    }

    override fun initView(view: View) {
        val listFragment = ListExampleFragment()
        pagerItem1Binding.refresh.setOnClickListener { listFragment.refresh() }
        childFragmentManager.beginTransaction().add(R.id.container, listFragment).commit()
    }
}

class ListExampleFragment : ListFragment<DefaultItem<HomeModel>>() {

    override fun refreshEnabled(): Boolean = false

    private val handler = Handler(Looper.getMainLooper())

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
    }

    override fun rvExtra(recyclerView: LoadMoreRecyclerView) {
        recyclerView.canScrollDirection(LoadMoreRecyclerView.SCROLL_DIRECTION_TOP)
    }

    override fun onLoad(page: Int, callback: LoadDataCallback<DefaultItem<HomeModel>>) {
        handler.postDelayed({
            val list = mutableListOf<DefaultItem<HomeModel>>().apply {
                for (i in 0 until 5) {
                    add(homeModel(HomeModel("msg----")))
                }
            }
            callback.onSuccess(list, if (isRefreshing) true else mData.size + list.size < 14)
        }, 1000)
    }

}