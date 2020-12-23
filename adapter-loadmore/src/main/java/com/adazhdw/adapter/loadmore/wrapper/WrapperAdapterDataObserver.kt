package com.adazhdw.adapter.loadmore.wrapper

import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference

/**
 * author：adazhdw
 * date-time：2020/12/23 14:50
 * description：
 **/
class WrapperAdapterDataObserver(
    private val refSubscriber: WeakReference<Subscriber>,
    private val refSource: WeakReference<RecyclerView.Adapter<*>>
) : RecyclerView.AdapterDataObserver() {

    interface Subscriber {
        fun refNotifyDataSetChanged(source: RecyclerView.Adapter<*>)
        fun refNotifyItemRangeChanged(source: RecyclerView.Adapter<*>, positionStart: Int, itemCount: Int)
        fun refNotifyItemRangeChanged(source: RecyclerView.Adapter<*>, positionStart: Int, itemCount: Int, payload: Any?)
        fun refNotifyItemRangeInserted(source: RecyclerView.Adapter<*>, positionStart: Int, itemCount: Int)
        fun refNotifyItemRangeRemoved(source: RecyclerView.Adapter<*>, positionStart: Int, itemCount: Int)
        fun refNotifyItemMoved(source: RecyclerView.Adapter<*>, fromPosition: Int, toPosition: Int, itemCount: Int)
    }

    override fun onChanged() {
        val sub = refSubscriber.get()
        val source = refSource.get()
        if (sub != null && source != null)
            sub.refNotifyDataSetChanged(source)
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        val sub = refSubscriber.get()
        val source = refSource.get()
        if (sub != null && source != null)
            sub.refNotifyItemRangeChanged(source, positionStart, itemCount)
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        val sub = refSubscriber.get()
        val source = refSource.get()
        if (sub != null && source != null)
            sub.refNotifyItemRangeChanged(source, positionStart, itemCount, payload)
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        val sub = refSubscriber.get()
        val source = refSource.get()
        if (sub != null && source != null)
            sub.refNotifyItemRangeInserted(source, positionStart, itemCount)
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        val sub = refSubscriber.get()
        val source = refSource.get()
        if (sub != null && source != null)
            sub.refNotifyItemRangeRemoved(source, positionStart, itemCount)
    }

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        val sub = refSubscriber.get()
        val source = refSource.get()
        if (sub != null && source != null)
            sub.refNotifyItemMoved(source, fromPosition, toPosition, itemCount)
    }
}