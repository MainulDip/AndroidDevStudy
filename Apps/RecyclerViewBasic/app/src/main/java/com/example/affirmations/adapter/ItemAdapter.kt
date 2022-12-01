package com.example.affirmations.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.affirmations.R
import com.example.affirmations.model.Affirmation

class ItemAdapter (private val context: Context, private val dataSet: List<Affirmation>):
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    /**
     * ItemViewHolder is a nested class here
     * Since ItemViewHolder is only used by ItemAdapter, creating it inside ItemAdapter shows this relationship.
     * This is not mandatory, but it helps other developers understand the structure of your program.
     * a view holder represents a single list item view */
    class ItemViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.item_title)
        val imageView: ImageView = view.findViewById(R.id.item_image)
    }

    /**
     * The onCreateViewHolder()method is called by the layout manager to create new view holders for the RecyclerView
     * again a view holder represents a single list item view */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return ItemViewHolder(adapterLayout);
    }
    /**
     * onBindViewHolder method is called by the layout manager in order to replace the contents of a list item view */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataSet[position]
        holder.textView.text = context.resources.getString(item.stringResourceId)
        holder.imageView.setImageResource(item.imageResourceId)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}