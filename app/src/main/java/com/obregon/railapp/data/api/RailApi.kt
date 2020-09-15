package com.obregon.railapp.data.api

import com.obregon.railapp.data.*
import retrofit2.http.GET
import retrofit2.http.Query

interface RailApi {
    @GET("getAllStationsXML")
    suspend fun getAllStations(): StationResponse

    @GET("getCurrentTrainsXML")
    suspend fun getCurrentTrains():StationDataResponse

    @GET("getStationDataByNameXML")
    suspend fun getTrainsForStation(@Query("StationDesc") stationDesc:String):StationDataResponse

    @GET("getStationDataByNameXML")
    suspend fun getTrainsForStation(@Query("StationDesc") stationDesc:String,
                                              @Query("NumMins") numMins:Int):StationDataResponse

    @GET("getTrainMovementsXML")
    suspend fun getTrainMovements(@Query("TrainId") trainId:String,@Query("TrainDate") trainDate:String): TrainMovementsResponse
}