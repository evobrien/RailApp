package com.obregon.railapp.ui.search

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.obregon.railapp.R
import com.obregon.railapp.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.search_screen_layout.*

@AndroidEntryPoint
class TextSearchScreen: Fragment(R.layout.search_screen_layout), StationAdapter.CellClickListener {
    private val searchScreenViewModel: SearchScreenViewModel by viewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this.activity?.let { activity ->
            searchScreenViewModel.stationNames.observe(
                activity, { setupSearchView(it) }
            )
        }
    }


    private fun setupSearchView(searchItems: Array<String>) {

        this.activity?.let {
            val adapter = ArrayAdapter<String>(it, android.R.layout.select_dialog_item,searchItems)
            auto_complete_view.threshold = 1
            auto_complete_view.setAdapter(adapter)
        }

        val recentSearchList=mutableSetOf<String>()
        val stationAdapter=StationAdapter(recentSearchList,this)
        recent_searches.apply {
            layoutManager = LinearLayoutManager(context)
            adapter=stationAdapter
        }

        btn_submit.setOnClickListener {
            val stationName=auto_complete_view.text.toString()
            if(stationName.isNotEmpty()){
                navigate(stationName)
            }
        }
    }

    private fun navigate(stationName: String){
        (recent_searches.adapter as StationAdapter).addItem(stationName)
        (this.activity as MainActivity).navigateToHome(stationName)
    }

    override fun onCellClickListener(station: String) {
       navigate(station)
    }

}
