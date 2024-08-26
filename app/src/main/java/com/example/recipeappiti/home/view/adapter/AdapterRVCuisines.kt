package com.example.recipeappiti.home.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeappiti.R
import com.example.recipeappiti.home.model.Area
import com.google.android.material.chip.Chip

class AdapterRVCuisines(private val chipList: List<Area>) :
    RecyclerView.Adapter<AdapterRVCuisines.ChipViewHolder>() {

    private val selectedCuisines = mutableSetOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv_cuisines, parent, false)
        return ChipViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) {
        val chipText = chipList[position].strArea
        holder.chip.text = chipText

        holder.chip.isChecked = selectedCuisines.contains(chipText)

        holder.chip.setOnClickListener {
            if (selectedCuisines.contains(chipText)) {
                selectedCuisines.remove(chipText)
                holder.chip.isChecked = false
            } else {
                selectedCuisines.add(chipText)
                holder.chip.isChecked = true
            }

        }

    }

    override fun getItemCount(): Int = chipList.size

    class ChipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chip: Chip = itemView.findViewById(R.id.chipCuisines)
    }

    fun getSelectedCuisines() = selectedCuisines.toList() //as List<String>

}
