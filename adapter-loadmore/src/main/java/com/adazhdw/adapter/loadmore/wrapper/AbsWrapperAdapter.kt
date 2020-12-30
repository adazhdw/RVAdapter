package com.adazhdw.adapter.loadmore.wrapper

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adazhdw.adapter.core.IItem
import java.lang.ref.WeakReference

/**
 * FileName: AbsWrapperAdapter
 * Author: adazhdw
 * Date: 2020/12/24 10:36
 * Description:
 */
abstract class AbsWrapperAdapter<Item : IItem<*, VH>, VH : RecyclerView.ViewHolder>(private val adapter: RecyclerView.Adapter<VH>) :
    RecyclerView.Adapter<VH>() {

    abstract var insertItems: List<Item>

    protected var recyclerView: RecyclerView? = null

    /**Sets an LayoutInflater Cache cache to this fast adapter instance.*/
    private val layoutInflaterCache: SparseArray<WeakReference<LayoutInflater>> = SparseArray()

    /**Gets LayoutInflater*/
    protected fun getLayoutInflater(context: Context): LayoutInflater = layoutInflaterCache.get(0).get() ?: LayoutInflater.from(context)

    /** This method states if we should insert a custom element at the given position */
    abstract fun shouldInsertItemAtPosition(position: Int): Boolean

    /** This method calculates how many elements were already inserted before this position */
    abstract fun itemInsertedBeforeCount(position: Int): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        //TODO optimize
        val vh = insertItems.firstOrNull { item -> item.itemViewType == viewType }?.getViewHolder(parent, getLayoutInflater(parent.context))
        if (vh != null) return vh
        return adapter.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        //empty implementation as the one with the List payloads is already called
    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        if (position == RecyclerView.NO_POSITION) return
        if (shouldInsertItemAtPosition(position)) {
            getInsertItem(position)?.bindVH(holder, payloads)
        } else {
            adapter.onBindViewHolder(holder, position - itemInsertedBeforeCount(position), payloads)
        }
    }

    override fun getItemCount(): Int {
        val itemCount = adapter.itemCount
        return itemCount + itemInsertedBeforeCount(itemCount)
    }

    override fun getItemViewType(position: Int): Int {
        if (shouldInsertItemAtPosition(position)) {
            return getInsertItem(position)?.itemViewType ?: 0
        }
        return adapter.getItemViewType(position - itemInsertedBeforeCount(position))
    }

    private fun getInsertItem(position: Int): Item? {
        if (shouldInsertItemAtPosition(position))
            return insertItems[itemInsertedBeforeCount(position - 1)]
        return null
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        adapter.onAttachedToRecyclerView(recyclerView)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (shouldInsertItemAtPosition(position)) layoutManager.spanCount else 1
                }
            }
        }
        this.recyclerView = recyclerView
        layoutInflaterCache.put(0, WeakReference(LayoutInflater.from(recyclerView.context)))
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        adapter.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
        layoutInflaterCache.clear()
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

    override fun setHasStableIds(hasStableIds: Boolean) {
        adapter.setHasStableIds(hasStableIds)
    }

    fun getRealAdapter(): RecyclerView.Adapter<VH> {
        return adapter
    }

}