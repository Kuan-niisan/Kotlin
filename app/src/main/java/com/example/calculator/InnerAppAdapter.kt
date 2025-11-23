package com.example.calculator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InnerAppAdapter(
    private val appList: List<AppItem>,
    private val viewType: Int
) : RecyclerView.Adapter<InnerAppAdapter.InnerViewHolder>() {

    inner class InnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivAppIcon: ImageView = itemView.findViewById(R.id.ivAppIcon)
        val tvAppName: TextView = itemView.findViewById(R.id.tvAppName)
        val tvAppCategory: TextView? = itemView.findViewById(R.id.tvAppCategory)
        val tvAppRating: TextView? = itemView.findViewById(R.id.tvAppRating)
        val tvAppSize: TextView? = itemView.findViewById(R.id.tvAppSize)
    }

    override fun getItemViewType(position: Int): Int = viewType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
        val layout = if (viewType == AppCategory.TYPE_SPONSORED) R.layout.item_app_sponsored else R.layout.item_app_recommended
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return InnerViewHolder(view)
    }

    override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
        val app = appList[position]
        holder.ivAppIcon.setImageResource(app.iconResId)
        holder.tvAppName.text = app.name

        // Chỉ gán dữ liệu nếu view đó tồn tại trong layout
        holder.tvAppCategory?.text = app.category
        holder.tvAppRating?.text = "${app.rating} ★"
        holder.tvAppSize?.text = app.size
    }

    override fun getItemCount() = appList.size
}