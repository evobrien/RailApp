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
import java.time.LocalTime


class ScheduledTrainsScreenViewModel @ViewModelInject constructor(private val repository: RailRepository): ViewModel() {

    private var _error=MutableLiveData<String>()
    var error=_error

    private var _uiDataList=MutableLiveData<List<UiTrainDataGroup>>()
    var uiDataList:LiveData<List<UiTrainDataGroup>> = _uiDataList

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
        val trainGroups:MutableMap<String,UiTrainDataGroup> =HashMap()
        if(stationDataList.isNotEmpty()){
            for(stationDataItem in stationDataList){
                val key=stationDataItem.direction
                val uiDataItem = getUiTrainData(stationDataItem)
                if(trainGroups.containsKey(key)){
                   ((trainGroups[key]?.uiTrainDataList) as MutableList<UiTrainData>).add(uiDataItem)
                }else{
                    val listDataItem:MutableList<UiTrainData> =ArrayList()
                    listDataItem.add(uiDataItem)
                    trainGroups[key] = UiTrainDataGroup(key,listDataItem)
                }
            }
            _uiDataList.value=trainGroups.values.toList()
        }else{
            error.value= NO_DATA_AVAILABLE_ERROR
        }
    }

    private fun formatStatusMessage(timeStatusMessage:String,stationDataItem: StationData): String {
        return if (!stationDataItem.status.contains("No Information", true)) {
            "$timeStatusMessage\n ${stationDataItem.status} - ${stationDataItem.lastLocation}"
        } else {
            "$timeStatusMessage\n"
        }
    }

    private fun getUiTrainData(
        stationDataItem: StationData
    ): UiTrainData {
        val route="${stationDataItem.origin} to ${stationDataItem.destination}"
        val scheduledLater=isScheduledLater(stationDataItem.originTime)
        val timeStatusMessage=getLateMessage(stationDataItem.late,scheduledLater,stationDataItem.originTime)
        val statusMessage = formatStatusMessage(timeStatusMessage,stationDataItem)

        return UiTrainData(
            route = route,
            origin = stationDataItem.origin,
            destination = stationDataItem.destination,
            status = statusMessage,
            dueIn = stationDataItem.dueIn.toString(),
            late = (stationDataItem.late > 0),
            expArrival = stationDataItem.expArrival,
            expDepart = stationDataItem.expDepart,
            schArrival = stationDataItem.schArrival,
            schDepart = stationDataItem.schDepart,
            direction = stationDataItem.direction,
            trainType = stationDataItem.trainType,
            trainCode = stationDataItem.trainCode,
            originTime = stationDataItem.originTime,
            destinationTime = stationDataItem.destinationTime,
            scheduledLater = scheduledLater
        )
    }

    private fun isScheduledLater(originTimeString:String):Boolean{
        val originTime=LocalTime.parse(originTimeString)
        return originTime.isAfter(LocalTime.now())
    }

    private fun getLateMessage(isLate: Int, scheduledLater:Boolean,originTimeString:String):String{
        return if (isLate >1){
            "$isLate mins late"
        } else if (isLate==1){
            "$isLate min late"
        }else {
            if(scheduledLater){
                "Departs origin later - $originTimeString"
            }else{
                "On time"
            }
        }
    }

    fun reload(){
        getTrainData(currentTrain)
    }
}