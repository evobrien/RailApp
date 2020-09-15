package com.obregon.railapp.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavouriteStation(
    @PrimaryKey @ColumnInfo(name = "station_name") val stationName: String,
)
