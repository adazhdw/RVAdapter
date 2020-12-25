package com.grantgzd.rvadaptertest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.adazhdw.adapter.core.DefaultItem
import com.adazhdw.adapter.core.bind
import com.adazhdw.adapter.core.listAdapter
import com.adazhdw.adapter.loadmore.defaultLoadMoreListener
import com.adazhdw.adapter.loadmore.loadMoreAdapter
import com.grantgzd.rvadaptertest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val listAdapter = listAdapter {
            addData(homeModel(HomeModel("msg----")))
        }
        val loadMoreAdapter = loadMoreAdapter(listAdapter)

        loadMoreAdapter.bind(binding.recyclerview, GridLayoutManager(this, 2))
        binding.add.setOnClickListener {
            listAdapter.addData(homeBindModel(HomeModel("msg----")))
            listAdapter.scrollToBottom()
        }
        val list = mutableListOf<DefaultItem<HomeModel>>().apply {
            for (i in 0 until 5) {
                add(homeModel(HomeModel("msg----")))
            }
        }
        listAdapter.addData(list)

        //加载更多
        binding.recyclerview.defaultLoadMoreListener {
            binding.recyclerview.postDelayed({
                listAdapter.addData(list)
                loadMoreAdapter.loadComplete(hasMore = listAdapter.getData().size < 20)
            }, 1000)
        }
    }
}