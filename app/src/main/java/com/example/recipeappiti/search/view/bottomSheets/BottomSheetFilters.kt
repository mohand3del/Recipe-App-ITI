package com.example.recipeappiti.search.view.bottomSheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.recipeappiti.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.card.MaterialCardView

class BottomSheetFilters : BottomSheetDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_filters, container, false)

        val itemCategoryBtn: MaterialCardView = view.findViewById(R.id.item_category_btn)
        val itemCuisinesBtn: MaterialCardView = view.findViewById(R.id.item_cuisines_btn)

        val bottomSheetCategoryFilter = BottomSheetCategoryFilter()
        val bottomSheetCuisinesFilter = BottomSheetCuisinesFilter()

        itemCuisinesBtn.setOnClickListener {
            dismiss()
            bottomSheetCuisinesFilter.show(
                requireActivity().supportFragmentManager,
                BottomSheetCuisinesFilter.TAG
            )
        }
        itemCategoryBtn.setOnClickListener {
            dismiss()
            bottomSheetCategoryFilter.show(
                requireActivity().supportFragmentManager,
                BottomSheetCategoryFilter.TAG
            )
        }

        return view
    }

    companion object {
        const val TAG = "BottomSheetFilters"
    }


}