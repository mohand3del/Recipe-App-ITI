package com.example.recipeappiti.auth.register.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.recipeappiti.core.model.remote.Area

class CuisineAdapter(
    context: Context,
    private var cuisines: MutableList<Area> = mutableListOf(),
    private val resource: Int = android.R.layout.simple_list_item_1,
) : ArrayAdapter<Area>(context, resource, cuisines) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)
        val area = getItem(position)
        (view as TextView).text = area?.strArea.toString()
        return view
    }

    fun updateCuisines(newCuisines: List<Area>) {
        addAll(newCuisines)
        notifyDataSetChanged()
    }
}