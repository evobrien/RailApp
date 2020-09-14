package com.obregon.railapp.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.obregon.railapp.data.QueryResult
import com.obregon.railapp.data.StationData
import com.obregon.railapp.data.repository.RailRepository
import com.obregon.railapp.ui.NETWORK_ERROR
import com.obregon.railapp.ui.NO_DATA_AVAILABLE_ERROR
import com.obregon.railapp.ui.UNKNOWN_ERROR
import kotlinx.coroutines.launch
import java.io.IOException


class ScheduledTrainsScreenViewModel @ViewModelInject constructor(private val repository: RailRepository): ViewModel() {

    private var _error=MutableLiveData<String>()
    var error=_error

    private var _uiDataList=MutableLiveData<List<UiTrainListData>>()
    var uiDataList:LiveData<List<UiTrainListData>> = _uiDataList

    private var currentTrain:String=""

    fun getTrainData(stationDesc:String){
        currentTrain=stationDesc
        viewModelScope.launch {
            when (val result=repository.getStationData(stationDesc)){
                is QueryResult.Success ->processTrainData(result.successPayload as List<StationData>)
                is QueryResult.Failure->processError(result.exception)
            }
        }
    }

    private fun processError(exception: Exception){
        when(exception){
            is IOException -> _error.value=NETWORK_ERROR
            else -> _error.value=UNKNOWN_ERROR
        }
    }

    private fun processTrainData(stationDataList:List<StationData>){
        if(!stationDataList.isEmpty()){
            val dataList=ArrayList<UiTrainListData>()
            for(stationDataItem in stationDataList){
               val uiDataItem= UiTrainListData(RowType.TYPE_ROW, UiTrainData(origin = stationDataItem.origin,
                   destination = stationDataItem.destination,
                   lastLocation = stationDataItem.lastLocation,
                   status =stationDataItem.status,
                   dueIn = stationDataItem.dueIn.toString(),
                   late = getLate(stationDataItem.late),
                   expArrival = stationDataItem.expArrival,
                   expDepart = stationDataItem.expDepart,
                   schArrival = stationDataItem.schArrival,
                   schDepart = stationDataItem.schDepart,
                   direction = stationDataItem.direction,
                   trainType = stationDataItem.trainType,
                   trainCode=stationDataItem.trainCode,
                   originTime = stationDataItem.originTime,
                   destinationTime = stationDataItem.destinationTime ))
                dataList.add(uiDataItem)
            }
            _uiDataList.value=dataList
        }else{
            error.value= NO_DATA_AVAILABLE_ERROR
        }
    }

    private fun getLate(isLate: Int):String{
        return if (isLate > 0){
            "Y ($isLate Mins)"
        } else {
            "N"
        }
    }

    fun reload(){
        getTrainData(currentTrain)
    }
}