package com.adazhdw.adapter.loadmore.wrapper

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference

/**
 * author：adazhdw
 * date-time：2020/12/23 14:12
 * description：
 **/
open class WrapperAdapter<VH : RecyclerView.ViewHolder>(private val adapter: RecyclerView.Adapter<VH>) : RecyclerView.Adapter<VH>(),
    WrapperAdapterDataObserver.Subscriber {

    init {
        val observer = WrapperAdapterDataObserver(WeakReference(this), WeakReference(adapter))
        adapter.registerAdapterDataObserver(observer)
        super.setHasStableIds(adapter.hasStableIds())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return adapter.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        onBindViewHolder(holder, position, mutableListOf())
    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        adapter.onBindViewHolder(holder, position, payloads)
    }

    override fun getItemCount(): Int = adapter.itemCount

    override fun getItemId(position: Int): Long {
        return adapter.getItemId(position)
    }

    override fun getItemViewType(position: Int): Int {
        return adapter.getItemViewType(position)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        adapter.onAttachedToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        adapter.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onViewAttachedToWindow(holder: VH) {
        adapter.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: VH) {
        adapter.onViewDetachedFromWindow(holder)
    }

    override fun onViewRecycled(holder: VH) {
        adapter.onViewRecycled(holder)
    }

    override fun onFailedToRecycleView(holder: VH): Boolean {
        return adapter.onFailedToRecycleView(holder)
    }

    fun getRealAdapter(): RecyclerView.Adapter<VH> {
        return adapter
    }

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