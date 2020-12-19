package com.grantgzd.rvadaptertest

import android.widget.TextView
import com.adazhdw.adapter.binding.defaultBindingItem
import com.adazhdw.adapter.binding.getDataBinding
import com.adazhdw.adapter.core.defaultItem
import com.adazhdw.adapter.core.getItemData
import com.grantgzd.rvadaptertest.databinding.ItemHomeModelBindingBinding

/**
 * author：daguozhu
 * date-time：2020/12/16 10:25
 * description：
 **/

data class HomeModel(val msg: String)

fun homeModel(model: HomeModel) =
    defaultItem(R.layout.item_home_model, model, {
        val text = getView<TextView>(R.id.tv)
        onBindViewHolder {
            val data = getItemData<HomeModel>()
            text.text = (data?.msg + adapterPosition)
        }
    })

fun homeBindModel(model: HomeModel) =
    defaultBindingItem(R.layout.item_home_model_binding, model, {
        onBindViewHolder {
            val binding = this.getDataBinding() as ItemHomeModelBindingBinding
            val data = getItemData<HomeModel>()
            binding.tv.text = (data?.msg + adapterPosition)
        }
    })

