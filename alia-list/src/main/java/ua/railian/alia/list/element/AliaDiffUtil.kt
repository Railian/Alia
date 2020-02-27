package ua.railian.alia.list.element

import androidx.recyclerview.widget.DiffUtil
import ua.railian.alia.core.Alia
import ua.railian.alia.list.stage.AliaAdapterBuilder

class AliaDiffUtil<ITEM : Any>(
    private val areItemsTheSame: (old: ITEM, new: ITEM) -> Boolean,
    private val areContentTheSame: (old: ITEM, new: ITEM) -> Boolean
) : Alia.Element {

    class Builder<ITEM : Any> {

        var areItemsTheSame: (old: ITEM, new: ITEM) -> Boolean = { _, _ -> false }
        var areContentTheSame: (old: ITEM, new: ITEM) -> Boolean = { old, new -> old == new }

        fun areItemsTheSame(block: (old: ITEM, new: ITEM) -> Boolean) {
            areItemsTheSame = block
        }

        fun areContentTheSame(block: (old: ITEM, new: ITEM) -> Boolean) {
            areContentTheSame = block
        }

        fun build() = AliaDiffUtil(areItemsTheSame, areContentTheSame)
    }

    fun createCallback(oldItems: List<ITEM>, newItems: List<ITEM>) = object : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            areItemsTheSame(oldItems[oldItemPosition], newItems[newItemPosition])

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            areContentTheSame(oldItems[oldItemPosition], newItems[newItemPosition])

        override fun getOldListSize() = oldItems.size
        override fun getNewListSize() = newItems.size
    }
}

inline fun <reified ITEM : Any> AliaAdapterBuilder<in ITEM>.diffUtil(
    noinline builder: AliaDiffUtil.Builder<ITEM>.() -> Unit
) = AliaDiffUtil.Builder<ITEM>().apply(builder).build()