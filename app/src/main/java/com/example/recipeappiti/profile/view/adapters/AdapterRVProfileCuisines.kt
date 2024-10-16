package com.example.recipeappiti.profile.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeappiti.R
import com.example.recipeappiti.core.model.remote.Area
import com.google.android.material.chip.Chip

class AdapterRVProfileCuisines(
    private val chipList: List<Area>,
    private var lastCuisine: ((String?) -> Unit)?,
    private val userCuisines: List<String>?
) :
    RecyclerView.Adapter<AdapterRVProfileCuisines.ChipViewHolder>() {
    private val selectedCuisines = mutableSetOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv_cuisines, parent, false)
        return ChipViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) {
        val chip = chipList[position]
        val chipText = chip.strArea
        holder.chip.text = chipText

        if (userCuisines?.contains(chipText) == true) {
            selectedCuisines.add(chipText)
            holder.chip.isChecked = true
        }

        holder.chip.setOnClickListener {
            if (selectedCuisines.contains(chipText)) {
                selectedCuisines.remove(chipText)
                holder.chip.isChecked = false
            } else {
                selectedCuisines.add(chipText)
                holder.chip.isChecked = true
            }

            if (selectedCuisines.isEmpty())
                lastCuisine?.invoke(null)
            else
                lastCuisine?.invoke(selectedCuisines.firstOrNull())
        }
    }

    override fun getItemCount(): Int = chipList.size

    class ChipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chip: Chip = itemView.findViewById(R.id.chipCuisines)
    }

    fun getSelectedCuisines() = selectedCuisines.toList()
}
