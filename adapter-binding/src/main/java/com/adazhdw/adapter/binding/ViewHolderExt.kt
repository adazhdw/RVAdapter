package com.adazhdw.adapter.binding

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * author：adazhdw
 * date-time：2020/12/19 10:20
 * description：
 **/

inline fun <reified VB : ViewDataBinding> RecyclerView.ViewHolder.getDataBinding(): VB {
    return itemView.getTag(R.id.adapter_list_binding) as VB
}



