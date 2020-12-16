package com.grantgzd.rvadaptertest

import android.widget.TextView
import com.adazhdw.adapter.core.DefaultViewItem
import com.adazhdw.adapter.core.defaultViewItem

/**
 * author：daguozhu
 * date-time：2020/12/16 10:25
 * description：
 **/

data class HomeModel(val msg: String)

fun homeModel(model: HomeModel): DefaultViewItem<HomeModel> =
    defaultViewItem(R.layout.item_home_model, model, {
        getView<TextView>(R.id.tv).text = (model.msg + adapterPosition)
    })

