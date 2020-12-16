package com.adazhdw.adapter.core

import androidx.annotation.IntRange

/**
 * author：daguozhu
 * date-time：2020/12/15 15:18
 * description：继承自 AbsAdapter 的 数据管理类
 **/
open class ListAdapter : AbsAdapter<GenericViewItem>() {

    private var data: MutableList<GenericViewItem> = mutableListOf()

    override fun getItemCount(): Int = data.size

    override fun getItem(position: Int): GenericViewItem? = data[position]

    /**
     * 用于外部获取数据
     */
    fun getData(): MutableList<GenericViewItem> = data

    /**
     * 方便获取指定 @param{position} 的数据
     */
    fun getItemData(position: Int): GenericViewItem = this.data[position]

    /**
     * 设置新的数据源
     */
    fun setNewData(data: MutableList<GenericViewItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    /**
     * 设置新数据
     */
    fun setData(data: Collection<GenericViewItem>): Boolean {
        this.data.clear()
        val result = this.data.addAll(data)
        notifyDataSetChanged()
        return result
    }

    /**
     * 设置某一条为新数据
     */
    fun setData(@IntRange(from = 0) index: Int, data: GenericViewItem) {
        if (index >= this.data.size) return
        this.data[index] = data
        notifyItemChanged(index)
    }

    /**
     * 添加一条新数据
     */
    fun addData(data: GenericViewItem): Boolean {
        val result = this.data.add(data)
        notifyItemInserted(this.data.size)
        return result
    }

    /**
     * 添加列表数据
     */
    fun addData(data: Collection<GenericViewItem>): Boolean {
        val oldSize = itemCount
        val result = this.data.addAll(data)
        if (result) {
            notifyItemRangeInserted(oldSize, itemCount - oldSize)
        }
        return result
    }

    /**
     * 插入一条数据
     */
    fun addData(@IntRange(from = 0) index: Int, data: GenericViewItem) {
        if (index >= this.data.size) return
        this.data.add(index, data)
        notifyItemInserted(index)
    }

    /**
     * 插入列表数据
     */
    fun addData(@IntRange(from = 0) index: Int, data: Collection<GenericViewItem>): Boolean {
        if (index >= this.data.size) return false
        val result = this.data.addAll(index, data)
        notifyItemInserted(index)
        return result
    }

    /**
     * 移除指定位置的数据
     */
    fun removeAt(@IntRange(from = 0) index: Int) {
        if (index >= this.data.size) return
        this.data.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeRemoved(index, this.data.size - index)
    }

    /**
     * 移除一条数据
     */
    fun remove(data: GenericViewItem) {
        val index = this.data.indexOf(data)
        if (index == -1) return
        removeAt(index)
    }

    /**
     * 清除数据
     */
    fun clearData() {
        this.data.clear()
        notifyDataSetChanged()
    }

}