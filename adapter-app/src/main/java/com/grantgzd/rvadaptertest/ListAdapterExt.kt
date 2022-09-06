package com.grantgzd.rvadaptertest

import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.adazhdw.adapter.core.AbsItem
import com.adazhdw.adapter.core.ViewHolder
import com.adazhdw.adapter.core.getItemData

/**
 * author：daguozhu
 * date-time：2020/12/16 10:25
 * description：
 **/

data class HomeModel(val msg: String)

class HomeItem(override var data: HomeModel?) : AbsItem<HomeModel, HomeItem.HomeVH>() {
    override val layoutRes: Int
        get() = R.layout.item_home_model

    override fun getViewHolder(view: View): HomeVH {
        return HomeVH(view)
    }

    class HomeVH(itemView: View) : ViewHolder(itemView) {
        private val text = getView<TextView>(R.id.tv)
        override fun onBindViewHolder(position: Int, payloads: List<Any>) {
            val data = getItemData<HomeModel>() ?: return
            text.text = "${data.msg + position}"
            itemView.setOnClickListener {
                Toast.makeText(it.context, text.text.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

}
