package com.adazhdw.adapter.core

import androidx.annotation.IntRange

/**
 * author：adazhdw
 * date-time：2020/12/18 13:41
 * description：
 **/
open class ListAdapter : AbsAdapter<GenericItem>() {

    private var items = mutableListOf<GenericItem>()

    override fun getItem(position: Int): GenericItem {
        return items[position]
    }

    override fun getItemCount(): Int = items.size

    /**
     * 用于外部获取数据
     */
    fun getData(): MutableList<GenericItem> = items

    /**
     * 方便获取指定 @param{position} 的数据
     */
    fun getItemData(position: Int): GenericItem = this.items[position]

    /**
     * 设置新的数据源
     */
    fun setNewData(items: MutableList<GenericItem>) {
        this.items = items
        notifyDataSetChanged()
    }

    /**
     * 设置新数据
     */
    fun setData(items: Collection<GenericItem>): Boolean {
        this.items.clear()
        val result = this.items.addAll(items)
        notifyDataSetChanged()
        return result
    }

    /**
     * 设置某一条为新数据
     */
    fun setData(@IntRange(from = 0) index: Int, items: GenericItem) {
        if (index >= this.itemCount) return
        this.items[index] = items
        notifyItemChanged(index)
    }

    /**
     * 添加一条新数据
     */
    fun addData(items: GenericItem): Boolean {
        val result = this.items.add(items)
        notifyItemInserted(this.itemCount)
        return result
    }

    /**
     * 添加列表数据
     */
    fun addData(items: Collection<GenericItem>): Boolean {
        val oldSize = itemCount
        val result = this.items.addAll(items)
        if (result) {
            notifyItemRangeInserted(oldSize, itemCount - oldSize)
        }
        return result
    }

    /**
     * 插入一条数据
     */
    fun addData(@IntRange(from = 0) index: Int, items: GenericItem) {
        if (index >= this.itemCount) return
        this.items.add(index, items)
        notifyItemInserted(index)
    }

    /**
     * 插入列表数据
     */
    fun addData(@IntRange(from = 0) index: Int, items: Collection<GenericItem>): Boolean {
        if (index >= this.itemCount) return false
        val result = this.items.addAll(index, items)
        notifyItemInserted(index)
        return result
    }

    /**
     * 移除指定位置的数据
     */
    fun removeAt(@IntRange(from = 0) index: Int) {
        if (index >= this.items.size) return
        this.items.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeRemoved(index, this.items.size - index)
    }

    /**
     * 移除一条数据
     */
    fun remove(items: GenericItem) {
        val index = this.items.indexOf(items)
        if (index == -1) return
        removeAt(index)
    }

    /**
     * 清除数据
     */
    fun clearData() {
        this.items.clear()
        notifyDataSetChanged()
    }

    /**
     * 划到顶部
     */
    fun scrollToTop() {
        if (this.items.isNotEmpty()) {
            recyclerView?.scrollToPosition(0)
        }
    }

    /**
     * 划到底部
     */
    fun scrollToBottom() {
        if (this.items.isNotEmpty()) {
            recyclerView?.scrollToPosition(this.itemCount - 1)
        }
    }

    /**
     * smooth划到顶部
     */
    fun smoothScrollToTop() {
        if (this.items.isNotEmpty()) {
            recyclerView?.smoothScrollToPosition(0)
        }
    }

    /**
     * smooth划到底部
     */
    fun smoothScrollToBottom() {
        if (this.items.isNotEmpty()) {
            recyclerView?.smoothScrollToPosition(this.itemCount - 1)
        }
    }

}