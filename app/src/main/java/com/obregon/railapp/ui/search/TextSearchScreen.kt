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
class TextSearchScreen: Fragment(R.layout.search_screen_layout), FavouriteStationsAdapter.CellClickListener {
    private val searchScreenViewModel: SearchScreenViewModel by viewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this.activity?.let { activity ->
            searchScreenViewModel.stationNames.observe(
                activity, { doLayout(it) }
            )
        }
    }


    private fun doLayout(searchItems: Array<String>) {
        setSearchBox(searchItems)
        setFavouriteList()
        loadFavourites()
        setSubmitButton()
    }

    private fun setSearchBox(searchItems: Array<String>) {
        this.activity?.let {
            val adapter = ArrayAdapter<String>(it, android.R.layout.select_dialog_item, searchItems)
            auto_complete_view.threshold = 1
            auto_complete_view.setAdapter(adapter)
        }
    }

    private fun setFavouriteList() {
        val recentSearchList = mutableSetOf<String>()
        val stationAdapter = FavouriteStationsAdapter(recentSearchList, this)
        recent_searches.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = stationAdapter
        }
    }

    private fun setSubmitButton() {
        btn_submit.setOnClickListener {
            val stationName = auto_complete_view.text.toString()
            if (stationName.isNotEmpty()) {
                navigate(stationName)
            }
        }
    }

    private fun navigate(stationName: String){
        addFavourite(stationName)
        (this.activity as MainActivity).navigateToHome(stationName)
    }

    private fun addFavourite(stationName: String) {
        (recent_searches.adapter as FavouriteStationsAdapter).addItem(stationName)
    }

    private fun addAllFavourites(favourites:List<String>){
        for(favourite in favourites){
            addFavourite(favourite)
        }
    }

    override fun onCellClickListener(station: String) {
       navigate(station)
    }

    private fun loadFavourites(){
        activity?.let { activity ->
            searchScreenViewModel.favouriteStations.observe(activity,{
                addAllFavourites(it)
            })
        }
        searchScreenViewModel.getAllFavouriteStations()
    }

    private fun saveFavourites(){
        val stations=
            (recent_searches.adapter as FavouriteStationsAdapter).stations
        searchScreenViewModel.saveFavouriteStations(stations.toList())
    }

    override fun onPause() {
        saveFavourites()
        super.onPause()
    }

}
