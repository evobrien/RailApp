package com.obregon.railapp.ui.search

import androidx.annotation.VisibleForTesting
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.obregon.railapp.data.QueryResult
import com.obregon.railapp.data.Station
import com.obregon.railapp.data.repository.RailRepository
import com.obregon.railapp.ui.NETWORK_ERROR
import com.obregon.railapp.ui.UNKNOWN_ERROR
import kotlinx.coroutines.launch
import java.io.IOException

data class SearchItem (val id:Int,val name:String)

class SearchScreenViewModel @ViewModelInject constructor(private val railRepository: RailRepository):ViewModel() {

    private var _error= MutableLiveData<String>()
    var error:LiveData<String> = _error

    private var _stationNames= MutableLiveData<Array<String>>()
    var stationNames:LiveData<Array<String>> =_stationNames

    private var _stations = MutableLiveData<List<Station>>()
    var stations = _stations

    init {
        viewModelScope.launch {
            when( val result=railRepository.getStations()){
                is QueryResult.Success -> processStations(result.successPayload as List<Station>)
                is QueryResult.Failure-> processError(result.exception)
            }
        }
    }

    @VisibleForTesting
    fun processError(exception: Exception){
        when(exception){
            is IOException -> _error.value= NETWORK_ERROR
            else -> _error.value= UNKNOWN_ERROR
        }
    }

    @VisibleForTesting
    fun processStations(stations:List<Station>){

        val sortedStations=stations.toSortedSet { a, b ->
            when {
                a.stationDesc > b.stationDesc -> 1
                a.stationDesc < b.stationDesc -> -1
                else -> 0
            }
        }
        val stationNames=arrayOfNulls<String>(sortedStations.size)
        sortedStations.forEachIndexed { index ,item->stationNames[index]=item.stationDesc }
        _stations.value=sortedStations.toList()
        _stationNames.value=stationNames as Array<String>
    }


}