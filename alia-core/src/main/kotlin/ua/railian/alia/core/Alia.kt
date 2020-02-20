@file:Suppress("MemberVisibilityCanBePrivate", "Unused")

package ua.railian.alia.core

object Alia {

    interface Element {
        infix fun join(core: Core): Core = Core(core.elements + this)
        infix fun leave(core: Core): Core = Core(core.elements - this)
    }

    data class Core(val elements: List<Element> = emptyList()) {
        companion object;
        infix operator fun plus(element: Element): Core = element join this
        infix operator fun minus(element: Element): Core = element leave this
    }

    abstract class Stage<T>(private val creator: (Core) -> T) {
        abstract val core: Core
        infix operator fun plus(element: Element): T = creator.invoke(core + element)
        infix operator fun minus(element: Element): T = creator.invoke(core - element)
    }

    abstract class Builder<T>(private var core: Core, private val creator: (Core) -> T) {
        operator fun Element.unaryPlus(): Unit = let { core += this }
        operator fun Element.unaryMinus(): Unit = let { core -= this }
        fun build(): T = creator.invoke(core)
    }
}