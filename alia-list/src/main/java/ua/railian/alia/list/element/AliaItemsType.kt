package ua.railian.alia.list.element

import ua.railian.alia.core.Alia

class AliaItemsType<ITEM>(val type: Class<ITEM>) : Alia.Element {

    override fun join(core: Alia.Core) = Alia.Core(core.elements.toMutableList().apply {
        if (core.elements.filterIsInstance<AliaItemsType<*>>().isNotEmpty())
            throw RuntimeException("Alia items type has already set. It can be set only once.")
        add(AliaItemsType(type))
    })
}