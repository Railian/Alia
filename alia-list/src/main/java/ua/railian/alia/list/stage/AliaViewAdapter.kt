package ua.railian.alia.list.stage

import ua.railian.alia.core.Alia
import ua.railian.alia.list.element.AliaInitialItems
import ua.railian.alia.list.element.AliaItemsType

class AliaListViewAdapter<ITEM : Any>(override val core: Alia.Core) :
    Alia.Stage<AliaListViewAdapter<ITEM>>(::AliaListViewAdapter)

class AliaAdapterBuilder<ITEM : Any>(
    itemsType: Class<ITEM>,
    initialItems: List<ITEM>? = null
) : Alia.Builder<AliaListViewAdapter<ITEM>>(
    core = createInitialCore(
        itemsType,
        initialItems
    ),
    creator = ::AliaListViewAdapter
) {
    companion object {
        private fun <ITEM : Any> createInitialCore(
            itemsType: Class<ITEM>,
            initialItems: List<ITEM>?
        ): Alia.Core {
            val elements = mutableListOf<Alia.Element>()
            elements += AliaItemsType(itemsType)
            initialItems.takeUnless { it.isNullOrEmpty() }
                ?.let { elements += AliaInitialItems(it) }
            return Alia.Core(elements)
        }
    }
}

inline fun <reified ITEM : Any> adapter(
    initialItems: List<ITEM>? = null,
    builder: AliaAdapterBuilder<ITEM>.() -> Unit
): AliaListViewAdapter<ITEM> = AliaAdapterBuilder(
    itemsType = ITEM::class.java,
    initialItems = initialItems
).apply(builder).build()

// TODO write extension for Alia.Element -> Element + Element