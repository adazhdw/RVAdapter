package com.adazhdw.adapter.core

import android.util.SparseArray

/**
 * author：adazhdw
 * date-time：2020/12/18 13:56
 * description：The default implementation to cache the viewholder factories.
 **/

class DefaultItemVHFactoryCache<ItemVHFactory : GenericItemVHFactory> : ItemVHFactoryCache<ItemVHFactory> {

    // we remember all possible types so we can create a new view efficiently
    private val factoryArray = SparseArray<ItemVHFactory>()

    override fun register(type: Int, item: ItemVHFactory): Boolean {
        if (factoryArray.indexOfKey(type) < 0) {
            factoryArray.put(type, item)
            return true
        }
        return false
    }

    override fun get(type: Int): ItemVHFactory {
        return factoryArray.get(type)
    }

    override fun contains(type: Int): Boolean = factoryArray.indexOfKey(type) >= 0

    override fun clear() {
        factoryArray.clear()
    }
}
