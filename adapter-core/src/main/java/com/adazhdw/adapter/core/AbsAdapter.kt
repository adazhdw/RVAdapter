package com.adazhdw.adapter.core

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adazhdw.rv.adapter.R
import java.lang.ref.WeakReference

/**
 * author：daguozhu
 * date-time：2020/12/15 9:26
 * description：继承自 RecyclerView.Adapter 抽象类
 **/

@Suppress("UNCHECKED_CAST")
abstract class AbsAdapter<Item : GenericViewItem> : RecyclerView.Adapter<RecyclerView.ViewHolder>(), IAdapter<Item> {

    protected var mContext: Context? = null
    protected val inflater: LayoutInflater by lazy { LayoutInflater.from(mContext) }
    protected var recyclerView: RecyclerView? = null
    private val defaultViewItemVHFactoryCache = DefaultItemVHFactoryCache<ItemVHFactory<*>>()
    private val layoutInflaterCache: SparseArray<WeakReference<LayoutInflater>> = SparseArray()

    /**
     * Register a new type factory into the TypeInstances to be able to efficiently create thew ViewHolders
     *
     * @param item an Item which will be shown in the list
     */
    fun registerItemFactory(type: Int, item: ItemVHFactory<*>) {
        this.defaultViewItemVHFactoryCache.register(type, item)
    }

    /**
     * Gets the TypeInstance remembered within the FastAdapter for an item
     *
     * @param type the int type of the item
     * @return the Item typeInstance
     */
    fun getItemVHFactory(type: Int): ItemVHFactory<*> {
        return this.defaultViewItemVHFactoryCache[type]
    }

    /**
     * Clears the internal mapper - be sure, to remap everything before going on
     */
    fun clearItemVHFactoryCache() {
        this.defaultViewItemVHFactoryCache.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val defaultViewHolder = this.getItemVHFactory(viewType).getViewHolder(
            parent, layoutInflaterCache.get(0).get() ?: LayoutInflater.from(parent.context)
        )
        defaultViewHolder.itemView.setTag(R.id.adapter, this)
        defaultViewHolder.itemView.setTag(R.id.adapter_recyclerView, recyclerView)
        return defaultViewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (position != RecyclerView.NO_POSITION) {
            holder.itemView.setTag(R.id.adapter, this)
            holder.itemView.setTag(R.id.adapter_recyclerView, recyclerView)
            getItem(position).run {
                this as ViewItem<*, RecyclerView.ViewHolder>
                holder.itemView.setTag(R.id.adapter_item, this)
                bindVH(holder, payloads)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.let {
            if (!defaultViewItemVHFactoryCache.contains(it.itemViewType)) {
                registerItemFactory(it.itemViewType, it)
            }
            it.itemViewType
        } ?: super.getItemViewType(position)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        holder.getViewItem<ViewItem<*, RecyclerView.ViewHolder>>()?.apply {
            unBindVH(holder)
        }
        holder.itemView.setTag(R.id.adapter, null)
        holder.itemView.setTag(R.id.adapter_item, null)
        holder.itemView.setTag(R.id.adapter_recyclerView, null)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        layoutInflaterCache.append(0, WeakReference(LayoutInflater.from(recyclerView.context)))
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        layoutInflaterCache.clear()
        this.recyclerView = null
    }
}

