package com.example.calculator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PlayStoreAdapter(private val items: List<PlayStoreItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Các ViewHolder cho từng loại view
    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvCategoryTitle)
        val arrow: ImageView = view.findViewById(R.id.ivArrow)
    }
    class AppRowViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.ivAppIcon)
        val name: TextView = view.findViewById(R.id.tvAppName)
        val category: TextView = view.findViewById(R.id.tvAppCategory)
        val rating: TextView = view.findViewById(R.id.tvAppRating)
        val size: TextView = view.findViewById(R.id.tvAppSize)
    }
    class HorizontalListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val innerRv: RecyclerView = view.findViewById(R.id.rvInner)
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is PlayStoreItem.Header -> 0
            is PlayStoreItem.AppRow -> 1
            is PlayStoreItem.HorizontalList -> 2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            0 -> HeaderViewHolder(inflater.inflate(R.layout.item_category_header, parent, false))
            1 -> AppRowViewHolder(inflater.inflate(R.layout.item_app_sponsored, parent, false))
            else -> HorizontalListViewHolder(inflater.inflate(R.layout.item_category_horizontal_list, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is PlayStoreItem.Header -> {
                val vh = holder as HeaderViewHolder
                vh.title.text = item.title
                vh.arrow.visibility = if (item.showArrow) View.VISIBLE else View.GONE
            }
            is PlayStoreItem.AppRow -> {
                val vh = holder as AppRowViewHolder
                val app = item.app
                vh.icon.setImageResource(app.iconResId)
                vh.name.text = app.name
                vh.category.text = app.category
                vh.rating.text = app.rating.toString()
                vh.size.text = app.size
            }
            is PlayStoreItem.HorizontalList -> {
                val vh = holder as HorizontalListViewHolder
                vh.innerRv.layoutManager = LinearLayoutManager(vh.itemView.context, LinearLayoutManager.HORIZONTAL, false)
                vh.innerRv.adapter = InnerAppAdapter(item.category.apps, item.category.viewType)
            }
        }
    }

    override fun getItemCount(): Int = items.size
}