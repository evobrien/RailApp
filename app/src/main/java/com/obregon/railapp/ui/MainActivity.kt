package com.obregon.railapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.obregon.railapp.R
import com.obregon.railapp.ui.home.ScheduledTrainsScreen
import com.obregon.railapp.ui.host.HostScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.main_activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setInitialLayout()
    }

    private fun setInitialLayout(){
        this.supportFragmentManager.beginTransaction()
            .add(
                R.id.content,
                HostScreen(),
                HostScreen::class.simpleName)
            .commit()
    }

    fun navigateToHome(stationName: String){
        this.supportFragmentManager.beginTransaction()
            .add(
                R.id.content,
                ScheduledTrainsScreen.newInstance(stationName),
                ScheduledTrainsScreen::class.simpleName)
            .addToBackStack(ScheduledTrainsScreen::class.simpleName)
            .commit()

    }

}