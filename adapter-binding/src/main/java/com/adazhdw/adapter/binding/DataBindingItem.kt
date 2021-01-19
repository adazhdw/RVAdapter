package com.adazhdw.adapter.binding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.adazhdw.adapter.core.DefaultItem
import com.adazhdw.adapter.core.DefaultViewHolder

/**
 * author：adazhdw
 * date-time：2020/12/18 17:46
 * description：
 **/

class DataBindingItem<M : Any>(override val layoutRes: Int) : DefaultItem<M>(layoutRes) {
    override fun getItemView(parent: ViewGroup, layoutInflater: LayoutInflater): View {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, layoutRes, parent, false)
        val view = binding.root
        view.setTag(R.id.adapter_list_binding, binding)
        return view
    }

    override fun bindVH(holder: DefaultViewHolder, payloads: List<Any>) {
        holder.getDataBinding<ViewDataBinding>().executePendingBindings()
    }

    override fun unbindVH(holder: DefaultViewHolder) {
        holder.getDataBinding<ViewDataBinding>().unbind()
    }

}




