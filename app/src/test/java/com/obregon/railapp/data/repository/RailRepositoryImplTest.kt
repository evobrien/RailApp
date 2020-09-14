package com.obregon.railapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.obregon.railapp.data.*
import com.obregon.railapp.data.api.RailApi
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.*

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class RailRepositoryImplTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    @MockK
    lateinit var railApi:RailApi
    lateinit var railRepository: RailRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        railRepository=RailRepositoryImpl(railApi)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getStationsSuccess() = runBlocking{
        val station=Station(stationDesc = "stationDesc1",
           stationAlias = "stationAlias1",
           stationLatitude = "53.0",
           stationLongitude = "53.0",
           stationCode ="StationCode",
           stationId = 1 )
        val stations= mutableListOf<Station>(station)
        val response =StationResponse(stations)
        coEvery { railApi.getAllStations() }.returns(response)
        val queryResult=railRepository.getStations()
        assertTrue(queryResult is QueryResult.Success)
        when(queryResult){
            is QueryResult.Success -> assertTrue(queryResult.successPayload == response.list)
            is QueryResult.Failure -> fail()
        }
    }

    @Test
    fun getStationsReturnsSuccessOnNullList() = runBlocking{
        val response =StationResponse(null)
        coEvery { railApi.getAllStations() }.returns(response)
        val queryResult=railRepository.getStations()
        assertTrue(queryResult is QueryResult.Success)
        val expected = ArrayList<Station>()
        when(queryResult){
            is QueryResult.Success -> assertTrue(queryResult.successPayload == expected)
            is QueryResult.Failure -> fail()
        }
    }

    @Test
    fun getStationsFailed() = runBlocking{
        coEvery { railApi.getAllStations() }.throws(Exception())
        val queryResult=railRepository.getStations()
        assertTrue(queryResult is QueryResult.Failure)
    }

    @Test
    fun getStationDataSuccess()= runBlocking {
        val stationName="stationName"
        val response=getMockStationDataResponse()
        coEvery { railApi.getTrainsForStation(stationName)}.returns(response)
        val queryResult= railRepository.getStationData(stationName)
        assertTrue(queryResult is QueryResult.Success)
        when(queryResult){
            is QueryResult.Success -> assertTrue(queryResult.successPayload == response.list)
            is QueryResult.Failure -> fail()
        }
    }

    @Test
    fun getStationDataReturnsSuccessOnNullList()= runBlocking {
        val stationName="stationName"
        val response=StationDataResponse(null)
        coEvery { railApi.getTrainsForStation(stationName)}.returns(response)
        val queryResult= railRepository.getStationData(stationName)
        assertTrue(queryResult is QueryResult.Success)
        val expected = ArrayList<StationData>()
        when(queryResult){
            is QueryResult.Success -> assertTrue(queryResult.successPayload == expected)
            is QueryResult.Failure -> fail()
        }
    }

    @Test
    fun getStationDataFailed()= runBlocking {
        val stationName="stationName"
        coEvery { railApi.getTrainsForStation(stationName)}.throws(Exception())
        val queryResult= railRepository.getStationData(stationName)
        assertTrue(queryResult is QueryResult.Failure)
    }

    @Test
    fun getCurrentTrainsSuccess() = runBlocking {
        val response=getMockStationDataResponse()
        coEvery {railApi.getCurrentTrains()}.returns(response)
        val queryResult= railRepository.getCurrentTrains()
        assertTrue(queryResult is QueryResult.Success)
        when(queryResult){
            is QueryResult.Success -> assertTrue(queryResult.successPayload == response.list)
            is QueryResult.Failure -> fail()
        }
    }

    @Test
    fun getCurrentTrainsReturnsSuccessOnNullList() = runBlocking {
        val response=StationDataResponse(null)
        coEvery {railApi.getCurrentTrains()}.returns(response)
        val queryResult= railRepository.getCurrentTrains()
        val expected = ArrayList<StationData>()
        assertTrue(queryResult is QueryResult.Success)
        when(queryResult){
            is QueryResult.Success -> assertTrue(queryResult.successPayload == expected)
            is QueryResult.Failure -> fail()
        }
    }

    @Test
    fun getCurrentTrainsFailed() = runBlocking {
        coEvery {railApi.getCurrentTrains()}.throws(Exception())
        val queryResult= railRepository.getCurrentTrains()
        assertTrue(queryResult is QueryResult.Failure)
    }

    @Test
    fun getTrainsForStationWithinMinsSuccess() = runBlocking  {
        val stationDesc="StationName"
        val numMins=1
        val response=getMockStationDataResponse()
        coEvery {railApi.getTrainsForStation(stationDesc,numMins)}.returns(response)
        val queryResult =railRepository.getTrainsForStationWithinMins(stationDesc,numMins)
        assertTrue(queryResult is QueryResult.Success)
        when(queryResult){
            is QueryResult.Success -> assertTrue(queryResult.successPayload == response.list)
            is QueryResult.Failure -> fail()
        }
    }

    @Test
    fun getTrainsForStationWithinMinsReturnsSuccessOnNull() = runBlocking  {
        val stationDesc="StationName"
        val numMins=1
        val response=StationDataResponse(null)
        coEvery {railApi.getTrainsForStation(stationDesc,numMins)}.returns(response)
        val queryResult =railRepository.getTrainsForStationWithinMins(stationDesc,numMins)
        val expected=ArrayList<StationData>()
        assertTrue(queryResult is QueryResult.Success)
        when(queryResult){
            is QueryResult.Success -> assertTrue(queryResult.successPayload == expected)
            is QueryResult.Failure -> fail()
        }
    }

    @Test
    fun getTrainsForStationWithinMinsFailed() = runBlocking  {
        val stationDesc="StationName"
        val numMins=1
        coEvery {railApi.getTrainsForStation(stationDesc,numMins)}.throws(Exception())
        val queryResult =railRepository.getTrainsForStationWithinMins(stationDesc,numMins)
        assertTrue(queryResult is QueryResult.Failure)
    }

    @Test
    fun getTrainMovementsSuccess() = runBlocking {
        val trainId="1"
        val trainDate="5 Sep 2020"
        val trainMovement=getMockTrainMovement()
        val trainMovementList= mutableListOf(trainMovement)
        val response=TrainMovementsResponse(trainMovementList)
        coEvery {railApi.getTrainMovements(trainId = trainId,trainDate = trainDate )}.returns(response)

       val queryResult= railRepository.getTrainMovements(trainId,trainDate)
        assertTrue(queryResult is QueryResult.Success)
        when(queryResult){
            is QueryResult.Success -> assertTrue(queryResult.successPayload == response.list)
            is QueryResult.Failure -> fail()
        }
    }

    @Test
    fun getTrainMovementsReturnsSuccessOnNullList() = runBlocking {
        val trainId="1"
        val trainDate="5 Sep 2020"

        val response=TrainMovementsResponse(null)
        coEvery {railApi.getTrainMovements(trainId = trainId,trainDate = trainDate )}.returns(response)
        val queryResult= railRepository.getTrainMovements(trainId,trainDate)

        val expected =ArrayList<TrainMovement>()
        assertTrue(queryResult is QueryResult.Success)
        when(queryResult){
            is QueryResult.Success -> assertTrue(queryResult.successPayload == expected)
            is QueryResult.Failure -> fail()
        }
    }

    @Test
    fun  getTrainMovementsFailed() = runBlocking  {
        val trainId="1"
        val trainDate="5 Sep 2020"
        coEvery {railApi.getTrainMovements(trainId = trainId,trainDate = trainDate )}.throws(Exception())
        val queryResult= railRepository.getTrainMovements(trainId,trainId)
        assertTrue(queryResult is QueryResult.Failure)
    }

    private fun getMockTrainMovement():TrainMovement{
        return TrainMovement(trainCode = "CODE",trainDate = "01/01/2020",
            locationCode = "locationCode",locationFullName = "locFullName",
            locationOrder = 1,locationType = 1,trainOrigin = "trainOrigin",
            trainDestination = "trainDestination",scheduledArrival = "01:01",
            scheduledDeparture = "00:01", expectedArrival = "01:02",expectedDeparture ="00:02",
            arrival = "arrival", departure = "departure",autoArrival = 1,autoDepart = 1,
            stopType = "stopType")
    }

    private fun getMockStationData():StationData{
       return StationData(locationType = "locationType1",
            serverTime = "serverTime1",
            trainType = "TrainType",
            late=0,
            originTime = "14:00",schArrival = "16:00",schDepart = "14:00",origin = "origin",
            expArrival = "16:05",direction = "outbound",destinationTime = "16:05",
            queryTime = "16:05", stationFullName = "staionFullName",
            status = "status", dueIn = 1,expDepart = "14:00",stationCode = "stationCode",
            trainCode = "trainCode",trainDate = "trainDate",
            destination = "destination",lastLocation = "lastLocations")
    }

    private fun getMockStationDataResponse():StationDataResponse{
        val stationDataList= mutableListOf<StationData>(getMockStationData())
        return StationDataResponse(stationDataList)
    }
}