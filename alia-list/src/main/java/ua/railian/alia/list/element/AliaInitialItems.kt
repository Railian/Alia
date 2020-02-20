package ua.railian.alia.list.element

import ua.railian.alia.core.Alia

class AliaInitialItems<ITEM>(val items: List<ITEM>) : Alia.Element {

    override fun join(core: Alia.Core) = Alia.Core(core.elements.toMutableList().apply {
        if (core.elements.filterIsInstance<AliaInitialItems<*>>().isNotEmpty())
            throw IllegalArgumentException("Alia initial items have already set")
        add(AliaInitialItems(items))
    })
}