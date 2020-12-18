package com.grantgzd.rvadaptertest

import android.widget.TextView
import com.adazhdw.adapter.core.DefaultItem
import com.adazhdw.adapter.core.defaultItem

/**
 * author：daguozhu
 * date-time：2020/12/16 10:25
 * description：
 **/

data class HomeModel(val msg: String)

fun homeModel(model: HomeModel): DefaultItem<HomeModel> =
    defaultItem(R.layout.item_home_model, model, {
        getView<TextView>(R.id.tv).text = (model.msg + adapterPosition)
    })

