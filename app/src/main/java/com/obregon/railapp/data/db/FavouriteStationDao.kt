package com.obregon.railapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavouriteStationDao {

    @Query("SELECT station_name FROM FavouriteStation")
    suspend fun getAll(): List<String>

    @Query("DELETE FROM FavouriteStation")
    suspend fun deleteAll()

    @Query("DELETE FROM FavouriteStation WHERE station_name=:station")
    suspend fun delete(station: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg station: FavouriteStation)

}