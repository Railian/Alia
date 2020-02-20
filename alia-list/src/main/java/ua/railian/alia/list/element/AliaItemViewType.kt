@file:Suppress("unused")

package ua.railian.alia.list.element

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import kotlinx.android.synthetic.main.simple_text_view.view.*
import ua.railian.alia.core.Alia
import ua.railian.alia.list.R
import ua.railian.alia.list.stage.AliaAdapterBuilder

class AliaItemViewType<ITEM : Any, VIEW : View>(
    val itemClass: Class<ITEM>,
    val condition: (ITEM) -> Boolean,
    val viewCreator: ViewCreatorParams.() -> VIEW,
    val binder: VIEW.(ITEM) -> Unit
) : Alia.Element {

    class ViewCreatorParams(
        val context: Context,
        val inflater: LayoutInflater,
        val root: ViewGroup?
    )
}

inline fun <reified ITEM : Any> AliaAdapterBuilder<in ITEM>.itemViewType(
    @LayoutRes viewResId: Int,
    noinline condition: (ITEM) -> Boolean = { true },
    noinline binder: View.(ITEM) -> Unit = {}
): AliaItemViewType<ITEM, View> = AliaItemViewType(
    itemClass = ITEM::class.java,
    condition = condition,
    viewCreator = { inflater.inflate(viewResId, root, false) },
    binder = binder
)

inline fun <reified ITEM : Any, VIEW : View> AliaAdapterBuilder<in ITEM>.itemViewType(
    noinline viewCreator: AliaItemViewType.ViewCreatorParams.() -> VIEW,
    noinline condition: (ITEM) -> Boolean = { true },
    noinline binder: VIEW.(ITEM) -> Unit = {}
): AliaItemViewType<ITEM, VIEW> = AliaItemViewType(
    itemClass = ITEM::class.java,
    condition = condition,
    viewCreator = viewCreator,
    binder = binder
)

inline fun <reified ITEM : Any> AliaAdapterBuilder<in ITEM>.simpleTextViewType(
    noinline condition: (ITEM) -> Boolean = { true },
    noinline binder: TextView.(ITEM) -> Unit = {}
) = itemViewType(
    R.layout.simple_text_view,
    condition = condition,
    binder = {
        textView.text = it.toString()
        binder.invoke(textView, it)
    }
)