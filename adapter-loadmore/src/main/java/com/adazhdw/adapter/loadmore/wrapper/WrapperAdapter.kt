package com.adazhdw.adapter.loadmore.wrapper

import androidx.recyclerview.widget.RecyclerView
import com.adazhdw.adapter.core.IItem
import java.lang.ref.WeakReference

/**
 * author：adazhdw
 * date-time：2020/12/23 14:12
 * description：
 **/
open class WrapperAdapter<Item : IItem<*, VH>, VH : RecyclerView.ViewHolder>(adapter: RecyclerView.Adapter<VH>) :
    AbsWrapperAdapter<Item, VH>(adapter),
    WrapperAdapterDataObserver.Subscriber {

    private var dataObserver: WrapperAdapterDataObserver? = null

    init {
        registerDataObserver()
    }

    private fun registerDataObserver() {
        if (dataObserver != null) {
            // 为原有的RecyclerAdapter移除数据监听对象
            getRealAdapter().unregisterAdapterDataObserver(dataObserver!!)
        } else {
            dataObserver = WrapperAdapterDataObserver(WeakReference(this), WeakReference(getRealAdapter()))
            getRealAdapter().registerAdapterDataObserver(dataObserver!!)
            dataObserver?.onChanged()
        }
    }

    override var insertItems: List<Item> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun shouldInsertItemAtPosition(position: Int): Boolean = false

    override fun itemInsertedBeforeCount(position: Int): Int = 0

    /**
     * 注册之后，会在adapter调用notifyXXX的时候，调用WrapperAdapter的notifyXXX
     */
    override fun refNotifyDataSetChanged(source: RecyclerView.Adapter<*>) {
        notifyDataSetChanged()
    }

    override fun refNotifyItemRangeChanged(source: RecyclerView.Adapter<*>, positionStart: Int, itemCount: Int) {
        notifyItemRangeChanged(positionStart, itemCount)
    }

    override fun refNotifyItemRangeChanged(source: RecyclerView.Adapter<*>, positionStart: Int, itemCount: Int, payload: Any?) {
        notifyItemRangeChanged(positionStart, itemCount, payload)
    }

    override fun refNotifyItemRangeInserted(source: RecyclerView.Adapter<*>, positionStart: Int, itemCount: Int) {
        notifyItemRangeInserted(positionStart, itemCount)
    }

    override fun refNotifyItemRangeRemoved(source: RecyclerView.Adapter<*>, positionStart: Int, itemCount: Int) {
        notifyItemRangeRemoved(positionStart, itemCount)
    }

    override fun refNotifyItemMoved(source: RecyclerView.Adapter<*>, fromPosition: Int, toPosition: Int, itemCount: Int) {
        notifyItemMoved(fromPosition, toPosition)
    }

}