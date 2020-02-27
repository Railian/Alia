package ua.railian.alia.list.native

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ua.railian.alia.list.AliaList
import ua.railian.alia.list.element.AliaDiffUtil
import ua.railian.alia.list.element.AliaInitialItems
import ua.railian.alia.list.element.AliaItemViewType
import ua.railian.alia.list.stage.AliaListViewAdapter
import kotlin.properties.Delegates

class AliaRecyclerViewAdapter<ITEM : Any>(
    private val aliaAdapter: AliaListViewAdapter<ITEM>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: List<ITEM> by Delegates.observable(initialItems) { _, oldItems, newItems ->
        if (aliaDiffUtil != null) {
            DiffUtil.calculateDiff(aliaDiffUtil.createCallback(oldItems, newItems), true)
                .dispatchUpdatesTo(this)
        } else notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.count()

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        aliaItemViewTypes.forEachIndexed { index, aliaViewType ->
            if (aliaViewType.itemClass.isAssignableFrom(item::class.java) &&
                aliaViewType.condition.invoke(item)
            ) return index
        }
        throw NullPointerException()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = aliaItemViewTypes[viewType].viewCreator.invoke(viewCreatorParams(parent))
        return object : RecyclerView.ViewHolder(itemView) {}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        aliaItemViewTypes[getItemViewType(position)].binder.invoke(holder.itemView, items[position])
    }

    private val initialItems: List<ITEM>
        get() = aliaAdapter.core.elements.filterIsInstance<AliaInitialItems<ITEM>>().singleOrNull()?.items
            ?: emptyList()

    private val aliaDiffUtil: AliaDiffUtil<ITEM>? =
        aliaAdapter.core.elements.filterIsInstance<AliaDiffUtil<ITEM>>().singleOrNull()

    private val aliaItemViewTypes: List<AliaItemViewType<ITEM, View>> =
        aliaAdapter.core.elements.filterIsInstance<AliaItemViewType<ITEM, View>>()

    private fun viewCreatorParams(parent: ViewGroup) = AliaItemViewType.ViewCreatorParams(
        context = parent.context,
        inflater = LayoutInflater.from(parent.context),
        root = parent
    )
}

infix fun <T : Any> AliaList.create(adapter: AliaListViewAdapter<T>): AliaRecyclerViewAdapter<T> =
    AliaRecyclerViewAdapter(adapter)

infix fun <T : Any> AliaRecyclerViewAdapter<T>.attachTo(recyclerView: RecyclerView): AliaRecyclerViewAdapter<T> {
    recyclerView.adapter = this
    if (recyclerView.layoutManager == null) {
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
    }
    return this
}

infix fun <T : Any> AliaRecyclerViewAdapter<T>.add(items: List<T>): AliaRecyclerViewAdapter<T> {
    this.items = items
    return this
}