package com.obregon.railapp.data

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementArray
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root


/*
* <objStation>
    <StationDesc>Fota</StationDesc>
    <StationAlias />
    <StationLatitude>51.896</StationLatitude>
    <StationLongitude>-8.3183</StationLongitude>
    <StationCode>FOTA </StationCode>
    <StationId>63</StationId>
  </objStation>*/
@Root(name="objStation",strict = false)
data class Station constructor(@field:Element(required=false,name="StationDesc")
                   var stationDesc:String="",
                   @field:Element(required=false,name="StationAlias")
                   var stationAlias:String="",
                   @field:Element(required=false,name="StationLatitude")
                   var stationLatitude:String="",
                   @field:Element(required=false,name="StationLongitude")
                   var stationLongitude:String="",
                   @field:Element(required=false,name="StationCode")
                   var stationCode:String="",
                   @field:Element(required=false,name="StationId")
                   var stationId:Int=0)

@Root(name="ArrayOfObjStation",strict = false)
data class StationResponse constructor(@field:ElementList(required = false,name="objStation",inline=true )  var list: List<Station>?=null)

@Root(name="objStationData",strict = false)
data class StationData constructor(
    @field:Element(required=false,name="Locationtype")
    var locationType:String="",
    @field:Element(required=false,name="Servertime")
    var serverTime:String="",
    @field:Element(required=false,name="Traintype")
    var trainType:String="",
    @field:Element(required=false,name="Late")
    var late:Int=0,
    @field:Element(required=false,name="Origintime")
    var originTime:String="",
    @field:Element(required=false,name="Scharrival")
    var schArrival:String="",
    @field:Element(required=false,name="Schdepart")
    var schDepart:String="",
    @field:Element(required=false,name="Origin")
    var origin:String="",
    @field:Element(required=false,name="Exparrival")
    var expArrival:String="",
    @field:Element(required=false,name="Direction")
    var direction:String="",
    @field:Element(required=false,name="Destinationtime")
    var destinationTime:String="",
    @field:Element(required=false,name="Querytime")
    var queryTime:String="",
    @field:Element(required=false,name="Stationfullname")
    var stationFullName:String="",
    @field:Element(required=false,name="Status")
    var status:String="",
    @field:Element(required=false,name="Duein")
    var dueIn:Int=0,
    @field:Element(required=false,name="Expdepart")
    var expDepart:String="",
    @field:Element(required=false,name="Stationcode")
    var stationCode:String="",
    @field:Element(required=false,name="Traincode")
    var trainCode:String="",
    @field:Element(required=false,name="Traindate")
    var trainDate:String="",
    @field:Element(required=false,name="Destination")
    var destination:String="",
    @field:Element(required=false,name="Lastlocation")
    var lastLocation:String="")

@Root(name="ArrayOfObjStationData",strict = false)
data class StationDataResponse constructor(@field:ElementList(required = false,name="objStationData",inline=true) var list: List<StationData>?=null)


/* <objTrainMovements>
    <TrainCode>E109 </TrainCode>
    <TrainDate>21 Dec 2011</TrainDate>
    <LocationCode>MHIDE</LocationCode>
    <LocationFullName>Malahide</LocationFullName>
    <LocationOrder>1</LocationOrder>
    <LocationType>O</LocationType>
    <TrainOrigin>Malahide</TrainOrigin>
    <TrainDestination>Greystones</TrainDestination>
    <ScheduledArrival>00:00:00</ScheduledArrival>
    <ScheduledDeparture>10:30:00</ScheduledDeparture>
    <ExpectedArrival>00:00:00</ExpectedArrival>
    <ExpectedDeparture>10:30:00</ExpectedDeparture>
    <Arrival>10:19:24</Arrival>
    <Departure>10:30:24</Departure>
    <AutoArrival>1</AutoArrival>
    <AutoDepart>1</AutoDepart>
    <StopType>-</StopType>
  </objTrainMovements>*/
@Root(name="objTrainMovements",strict = false)
data class TrainMovement constructor(
    @field:Element(required=false,name="TrainCode")val trainCode:String,
    @field:Element(required=false,name="TrainDate")val trainDate:String,
    @field:Element(required=false,name="LocationCode")val locationCode:String,
    @field:Element(required=false,name="LocationFullName")val locationFullName:String,
    @field:Element(required=false,name="LocationOrder")val locationOrder:Int,
    @field:Element(required=false,name="LocationType")val locationType:Int,
    @field:Element(required=false,name="TrainOrigin")val trainOrigin:String,
    @field:Element(required=false,name="TrainDestination")val trainDestination:String,
    @field:Element(required=false,name="ScheduledArrival")val scheduledArrival:String,
    @field:Element(required=false,name="ScheduledDeparture")val scheduledDeparture:String,
    @field:Element(required=false,name="ExpectedArrival")val expectedArrival:String,
    @field:Element(required=false,name="ExpectedDeparture")val expectedDeparture:String,
    @field:Element(required=false,name="Arrival")val arrival:String,
    @field:Element(required=false,name="Departure")val departure:String,
    @field:Element(required=false,name="AutoArrival")val autoArrival:Int,
    @field:Element(required=false,name="AutoDepart")val autoDepart:Int,
    @field:Element(required=false,name="StopType")val stopType:String)

@Root(name="ArrayOfObjTrainMovements",strict = false)
data class TrainMovementsResponse constructor(@field:ElementList(required = false,name="objTrainMovements",inline=true) var list: List<TrainMovement>?=null)