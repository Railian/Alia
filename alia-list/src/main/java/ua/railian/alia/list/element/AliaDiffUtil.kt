package ua.railian.alia.list.element

import androidx.recyclerview.widget.DiffUtil
import ua.railian.alia.core.Alia
import ua.railian.alia.list.stage.AliaAdapterBuilder

class AliaDiffUtil<ITEM : Any>(
    private val areItemsTheSame: (old: ITEM, new: ITEM) -> Boolean,
    private val areContentTheSame: (old: ITEM, new: ITEM) -> Boolean
) : Alia.Element {

    fun create(oldItems: List<ITEM>, newItems: List<ITEM>) = object : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            areItemsTheSame(oldItems[oldItemPosition], newItems[newItemPosition])

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            areContentTheSame(oldItems[oldItemPosition], newItems[newItemPosition])

        override fun getOldListSize() = oldItems.size
        override fun getNewListSize() = newItems.size
    }
}

inline fun <reified ITEM : Any> AliaAdapterBuilder<in ITEM>.diffUtil(
    noinline areContentTheSame: (old: ITEM, new: ITEM) -> Boolean = { old, new -> old == new },
    noinline areItemsTheSame: (old: ITEM, new: ITEM) -> Boolean
) = AliaDiffUtil(areItemsTheSame, areContentTheSame)