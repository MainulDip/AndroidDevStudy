package com.example.affirmations.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.affirmations.R
import com.example.affirmations.model.Affirmation

class ItemAdapter (private val context: Context, private val dataSet: List<Affirmation>):
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    /**
     * ItemViewHolder is a nested class here
     * Since ItemViewHolder is only used by ItemAdapter, creating it inside ItemAdapter shows this relationship.
     * This is not mandatory, but it helps other developers understand the structure of your program.*/

    class ItemViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        val textView: View = view.findViewById(R.id.item_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}