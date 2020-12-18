package com.adazhdw.adapter.core

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adazhdw.rv.adapter.R
import java.lang.ref.WeakReference

/**
 * author：adazhdw
 * date-time：2020/12/18 11:00
 * description：
 **/
@Suppress("UNCHECKED_CAST")
abstract class AbsAdapter<Item : GenericItem> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected var recyclerView: RecyclerView? = null

    /**Sets an item factory cache to this fast adapter instance.*/
    open var itemVHFactoryCache: ItemVHFactoryCache<IItemVHFactory<*>> = DefaultItemVHFactoryCache()

    /**Sets an LayoutInflater Cache cache to this fast adapter instance.*/
    private val layoutInflaterCache: SparseArray<WeakReference<LayoutInflater>> = SparseArray()

    /**legacy bindView mode. if activated we will forward onBindView without payloads to the method with payloads*/
    var legacyBindViewMode = false

    abstract fun getItem(position: Int): Item?

    /**Register a new type factory into the itemVHFactoryCache to be able to efficiently create thew ViewHolders*/
    fun registerItemFactory(type: Int, itemVHF: IItemVHFactory<*>) {
        this.itemVHFactoryCache.register(type, itemVHF)
    }

    /**Gets the IItemVHFactory remembered within the BaseAdapter for an item*/
    fun getItemVHFactory(type: Int): IItemVHFactory<*> = this.itemVHFactoryCache[type]

    /**Clears the internal mapper - be sure, to remap everything before going on*/
    fun clearItemVHFactoryCache() {
        this.itemVHFactoryCache.clear()
    }

    /*** Creates the ViewHolder by the viewType*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holder = getItemVHFactory(viewType)
            .getViewHolder(parent, layoutInflaterCache.get(0).get() ?: LayoutInflater.from(parent.context))
        holder.itemView.setTag(R.id.adapter, this@AbsAdapter)
        return holder
    }

    /*** Binds the data to the created ViewHolder and sets the listeners to the holder.itemView*/
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (legacyBindViewMode) {
            holder.itemView.setTag(R.id.adapter, this@AbsAdapter)
            (getItem(position) as? IItem<*, RecyclerView.ViewHolder>)?.run {
                holder.itemView.setTag(R.id.adapter_item, this@AbsAdapter)
                this.bindVH(holder, listOf())
            }
        }
    }

    /*** Binds the data to the created ViewHolder and sets the listeners to the holder.itemView*/
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (!legacyBindViewMode) {
            holder.itemView.setTag(R.id.adapter, this@AbsAdapter)
            (getItem(position) as? IItem<*, RecyclerView.ViewHolder>)?.run {
                holder.itemView.setTag(R.id.adapter_item, this@AbsAdapter)
                this.bindVH(holder, listOf())
            }
        }
        super.onBindViewHolder(holder, position, payloads)
    }

    /*** Finds the int ItemViewType from the IItem which exists at the given position*/
    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.let {
            if (!this.itemVHFactoryCache.contains(it.itemViewType)) {
                registerItemFactory(it.itemViewType, it)
            }
            it.itemViewType
        } ?: super.getItemViewType(position)
    }

    /*** Unbinds the data to the already existing ViewHolder*/
    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        holder.getItem<IItem<*, RecyclerView.ViewHolder>>()?.run {
            unbindVH(holder)
        }
        holder.itemView.setTag(R.id.adapter, null)
        holder.itemView.setTag(R.id.adapter_item, null)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
        this.layoutInflaterCache.put(0, WeakReference(LayoutInflater.from(recyclerView.context)))
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
        this.layoutInflaterCache.clear()
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.getItem<IItem<*, RecyclerView.ViewHolder>>()?.attachToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.getItem<IItem<*, RecyclerView.ViewHolder>>()?.detachFromWindow(holder)
    }

}