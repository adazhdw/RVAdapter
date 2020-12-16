package com.adazhdw.adapter.core

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Kotlin type alias to simplify usage for an all accepting item
 */
typealias GenericItemVHFactory = ItemVHFactory<out RecyclerView.ViewHolder>

/**
 * Defines the factory which is capable of creating the ViewHolder for a given Item
 */
interface ItemVHFactory<VH : RecyclerView.ViewHolder> {
    /** Generates a ViewHolder from this Item with the given parent */
    fun getViewHolder(parent: ViewGroup, layoutInflater: LayoutInflater): VH
}

/**
 * Defines the factory logic to generate ViewHolders for an item
 */
interface ItemVHFactoryCache<VHFactory : GenericItemVHFactory> {

    fun register(type: Int, item: VHFactory): Boolean

    operator fun get(type: Int): VHFactory

    fun contains(type: Int): Boolean

    fun clear()
}

class DefaultItemVHFactoryCache<VHFactory : GenericItemVHFactory> : ItemVHFactoryCache<VHFactory> {

    // we remember all possible types so we can create a new view efficiently
    private val typeInstances = SparseArray<VHFactory>()
    override fun register(type: Int, item: VHFactory): Boolean {
        if (typeInstances.indexOfKey(type) < 0) {
            typeInstances.put(type, item)
            return true
        }
        return false
    }

    override fun get(type: Int): VHFactory = typeInstances[type]

    override fun contains(type: Int): Boolean = typeInstances.indexOfKey(type) >= 0

    override fun clear() {
        typeInstances.clear()
    }

}

