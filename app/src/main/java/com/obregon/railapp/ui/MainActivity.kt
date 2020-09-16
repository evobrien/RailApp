package com.obregon.railapp.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.fragment.app.FragmentManager
import com.obregon.railapp.R
import com.obregon.railapp.ui.home.ScheduledTrainsScreen
import com.obregon.railapp.ui.host.HostScreen
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.main_activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActionBar()
        setInitialLayout()
    }

    private fun setActionBar(){
        Timber.i("BackstackEntryCount ${this.supportFragmentManager.backStackEntryCount}")
        if(this.supportFragmentManager.backStackEntryCount>0){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            this.supportActionBar?.setDisplayShowHomeEnabled(false)

        }else {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            this.supportActionBar?.setIcon(R.drawable.ic_drawer)
            this.supportActionBar?.setHomeButtonEnabled(true)
            this.supportActionBar?.setDisplayShowHomeEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        setActionBar()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home-> {
                this.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun setInitialLayout(){
        this.supportFragmentManager.beginTransaction()
            .add(
                R.id.content,
                HostScreen(),
                HostScreen::class.simpleName
            )
            .commit()
    }

    fun navigateToHome(stationName: String){
        this.supportFragmentManager.beginTransaction()
            .add(
                R.id.content,
                ScheduledTrainsScreen.newInstance(stationName),
                ScheduledTrainsScreen::class.simpleName
            )
            .addToBackStack(ScheduledTrainsScreen::class.simpleName)
            .commit()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        setActionBar()
    }

}