package com.grantgzd.rvadaptertest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.adazhdw.adapter.core.DefaultItem
import com.adazhdw.adapter.core.bind
import com.adazhdw.adapter.core.listAdapter
import com.grantgzd.rvadaptertest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by inflate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val listAdapter = listAdapter {
//            addData(homeModel(HomeModel("msg----")))
        }

        listAdapter.bind(binding.recyclerview)
        binding.add.setOnClickListener {
            listAdapter.addData(homeBindModel(HomeModel("msg----")))
            listAdapter.scrollToBottom()
        }
        val list = mutableListOf<DefaultItem<HomeModel>>().apply {
            for (i in 0..10) {
                add(homeModel(HomeModel("msg----")))
            }
        }
        listAdapter.addData(list)
    }
}