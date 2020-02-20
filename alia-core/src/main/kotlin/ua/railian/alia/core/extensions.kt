package ua.railian.alia.core

operator fun Alia.Core.Companion.invoke(vararg elements: Alia.Element): Alia.Core =
    Alia.Core(elements.toList())