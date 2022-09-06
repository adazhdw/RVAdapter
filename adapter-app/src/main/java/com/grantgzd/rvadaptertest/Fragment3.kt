package com.grantgzd.rvadaptertest

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adazhdw.adapter.list.ListFragmentEx
import com.adazhdw.ktlib.base.fragment.ViewBindingFragment
import com.grantgzd.rvadaptertest.databinding.LayoutViewPagerItem2Binding

/**
 * FileName: Fragment1
 * Author: adazhdw
 * Date: 2020/12/25 11:11
 * Description:
 * History:
 */
class Fragment3 : ViewBindingFragment<LayoutViewPagerItem2Binding>() {

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?): LayoutViewPagerItem2Binding {
        return LayoutViewPagerItem2Binding.inflate(inflater, container, false)
    }

    override fun initView(view: View) {
        val listFragment = ListExampleFragment3()
        viewBinding.refresh.setOnClickListener { listFragment.refresh() }
        childFragmentManager.beginTransaction().add(R.id.container, listFragment).commit()
    }
}

class ListExampleFragment3 : ListFragmentEx<HomeItem>() {

    private val handler = Handler(Looper.getMainLooper())

    override fun onLoad(page: Int, callback: LoadDataCallback<HomeItem>) {
        handler.postDelayed({
            val list = mutableListOf<HomeItem>().apply {
                for (i in 0 until 10) {
                    add(HomeItem(HomeModel("msg----")))
                }
            }
            callback.onSuccess(list, if (isRefreshing) true else mData.size + list.size < 20)
        }, 1000)
    }

}