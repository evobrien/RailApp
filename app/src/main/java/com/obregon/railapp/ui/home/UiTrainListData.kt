package com.obregon.railapp.ui.home

import android.os.Message
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/*
*   <Servertime>2020-09-03T23:55:15.143</Servertime>
    <Traincode>E703 </Traincode>
    <Stationfullname>Tara Street</Stationfullname>
    <Stationcode>TARA</Stationcode>
    <Querytime>23:55:15</Querytime>
    <Traindate>03 Sep 2020</Traindate>
    <Origin>Greystones</Origin>
    <Destination>Dublin Connolly</Destination>
    <Origintime>23:04</Origintime>
    <Destinationtime>00:02</Destinationtime>
    <Status>En Route</Status>
    <Lastlocation>Arrived Grand Canal Dock</Lastlocation>
    <Duein>3</Duein>
    <Late>-1</Late>
    <Exparrival>23:57</Exparrival>
    <Expdepart>23:58</Expdepart>
    <Scharrival>23:58</Scharrival>
    <Schdepart>23:59</Schdepart>
    <Direction>Northbound</Direction>
    <Traintype>DART</Traintype>
    <Locationtype>S</Locationtype>
* */
@Parcelize
data class UiTrainData (val route:String,val origin:String,
                                   val destination:String,
                                   val status:String,
                                   val dueIn:String,
                                   val late:Boolean,
                                   val expDepart:String,
                                   val expArrival:String,
                                   val schArrival:String,
                                   val schDepart:String,
                                   val direction:String,
                                   val trainType:String,
                                   val trainCode:String,
                                   val originTime:String,
                                   val destinationTime:String, val scheduledLater:Boolean):Parcelable
@Parcelize
data class UiTrainDataGroup(val direction:String,
                            val uiTrainDataList:List<UiTrainData>,
                            val errorMessage: String=""):Parcelable
