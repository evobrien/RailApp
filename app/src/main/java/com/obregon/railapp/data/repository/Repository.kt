package com.obregon.railapp.data.repository

import com.obregon.railapp.data.QueryResult
import com.obregon.railapp.data.QueryResult.*
import com.obregon.railapp.data.Station
import com.obregon.railapp.data.StationData
import com.obregon.railapp.data.TrainMovement
import com.obregon.railapp.data.api.RailApi
import javax.inject.Inject


interface RailRepository {
    suspend fun getStations(): QueryResult
    suspend fun getStationData(stationDesc: String): QueryResult
    suspend fun getCurrentTrains(): QueryResult
    suspend fun getTrainsForStationWithinMins(stationDesc:String, numMins:Int): QueryResult
    suspend fun getTrainMovements(trainId:String,trainDate:String):QueryResult
}

class RailRepositoryImpl @Inject constructor(val railApi: RailApi): RailRepository {
    override suspend fun getStations(): QueryResult {
        return try{
            var stations= railApi.getAllStations().list
            stations = stations ?: ArrayList()
            Success(stations)
        }catch (e:Exception){
            Failure(e)
        }
    }

    override suspend fun getStationData(stationDesc:String): QueryResult {
       return try{
           var stationData=railApi.getTrainsForStation(stationDesc).list
           stationData = stationData ?: ArrayList()
           Success(stationData)
       }catch(e:Exception){
           Failure(e)
       }
    }

    override suspend fun getCurrentTrains(): QueryResult {
        return try{
            var stationData=railApi.getCurrentTrains().list
            stationData = stationData ?: ArrayList()
            Success(stationData)
        }catch(e:Exception){
            Failure(e)
        }
    }

    override suspend fun getTrainsForStationWithinMins(
        stationDesc: String,
        numMins: Int
    ): QueryResult {
        return try{
            var stationData=railApi.getTrainsForStation(stationDesc,numMins).list
            stationData = stationData ?: ArrayList()
            Success(stationData)
        }catch(e:Exception){
            Failure(e)
        }
    }

    override suspend fun getTrainMovements(trainId:String,trainDate:String):QueryResult{
       return try{
           var trainMovements=railApi.getTrainMovements(trainId,trainDate).list
           trainMovements = trainMovements ?: ArrayList()
           Success(trainMovements)
        }catch(e:Exception){
            Failure(e)
        }
    }
}