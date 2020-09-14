package com.obregon.railapp.ui.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.obregon.railapp.data.QueryResult
import com.obregon.railapp.data.Station
import com.obregon.railapp.data.TrainMovement
import com.obregon.railapp.data.repository.RailRepository
import com.obregon.railapp.ui.NETWORK_ERROR
import com.obregon.railapp.ui.UNKNOWN_ERROR
import kotlinx.coroutines.launch
import java.io.IOException

class StopListViewModel @ViewModelInject constructor(val repository: RailRepository) :ViewModel(){

    private var _error=MutableLiveData<String>()
    var error: LiveData<String> = _error

    private var _trainMovements=MutableLiveData<List<TrainMovement>>()
    var trainMovement:LiveData<List<TrainMovement>> = _trainMovements

    fun getTrainMovements(trainId:String,trainDate:String){

        viewModelScope.launch {
            when( val result=repository.getTrainMovements(trainId,trainDate)){
                is QueryResult.Success -> processTrainMovements(result.successPayload as List<TrainMovement>)
                is QueryResult.Failure-> processError(result.exception)
            }
        }
    }

    fun processTrainMovements(movements:List<TrainMovement>){
        _trainMovements.value = movements
    }

    fun processError(exception:Exception){
        when(exception){
            is IOException -> _error.value= NETWORK_ERROR
            else -> _error.value= UNKNOWN_ERROR
        }
    }
}