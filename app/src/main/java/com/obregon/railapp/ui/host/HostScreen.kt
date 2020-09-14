package com.obregon.railapp.ui.host

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.obregon.railapp.R
import com.obregon.railapp.ui.search.MapSearchScreen
import com.obregon.railapp.ui.search.TextSearchScreen
import kotlinx.android.synthetic.main.host_screen_layout.*

class HostScreen : Fragment(R.layout.host_screen_layout) {

    private val titles=arrayOf("Search","Map")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pagerAdapter = PagerAdapter(this.childFragmentManager, this.lifecycle)
        pager.adapter=pagerAdapter
        TabLayoutMediator(tab_layout, pager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }
}

class PagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(
    fragmentManager,
    lifecycle
) {
    private val ITEM_COUNT=2
    private val MAP_FRAGMENT_TAB_POS=1

    override fun getItemCount(): Int {
        return ITEM_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return when (position){
            MAP_FRAGMENT_TAB_POS -> MapSearchScreen()
            else -> TextSearchScreen()
        }
    }
}