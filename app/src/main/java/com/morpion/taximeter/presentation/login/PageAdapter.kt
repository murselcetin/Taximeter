package com.morpion.taximeter.presentation.login

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.morpion.taximeter.R

class PageAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> PageFragment.newInstance(
                pageTypeEven,
                R.drawable.ic_route,
                R.drawable.ic_taximeter,
                R.string.page_one_text_top,
                R.string.page_one_text_bottom
            )
            1 -> PageFragment.newInstance(
                pageTypeOdd,
                R.drawable.ic_directions2,
                R.drawable.ic_navigation,
                R.string.page_two_text_top,
                R.string.page_two_text_bottom
            )
            2 -> PageFragment.newInstance(
                pageTypeEven,
                R.drawable.ic_taxi_service,
                R.drawable.ic_taxi_stands,
                R.string.page_three_text_top,
                R.string.page_three_text_bottom
            )
            3 -> PageFragment.newInstance(
                pageTypeOdd,
                R.drawable.ic_taxi_fee,
                R.drawable.ic_calculation,
                R.string.page_four_text_top,
                R.string.page_four_text_bottom
            )
            else -> return PageFragment.newInstance(
                pageTypeEven,
                R.drawable.ic_route,
                R.drawable.ic_taximeter,
                R.string.page_one_text_top,
                R.string.page_one_text_bottom
            )
        }
    }

    override fun getCount(): Int {
        return 4
    }

    companion object {
        const val pageTypeEven: Int = 0
        const val pageTypeOdd: Int = 1
    }
}