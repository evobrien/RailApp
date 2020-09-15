package com.obregon.railapp.ui.home

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.obregon.railapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.scheduled_screen_fragment.*
import kotlinx.android.synthetic.main.scheduled_trains_host.*

val STATION_KEY="STATION_NAME"

@AndroidEntryPoint
class ScheduledTrainsScreen:Fragment() {
    private val viewModel: ScheduledTrainsScreenViewModel by viewModels()

    companion object{
        fun newInstance(stationName:String):ScheduledTrainsScreen{
            val args=Bundle()
            args.putString(STATION_KEY,stationName)
            val homeScreenFragment=ScheduledTrainsScreen()
            homeScreenFragment.arguments=args
            return homeScreenFragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.scheduled_trains_host, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val args=arguments
        val station=args?.getString(STATION_KEY)
        station?.let {
            viewModel.getTrainData(station)
            selected_station.text=station
        }
        setObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.refresh->reload()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setObservers(){
        showProgress()
        viewModel.error.observeForever{handleError(it)}
        viewModel.uiDataList.observeForever{layoutTabs(it)}
    }

    private fun handleError(errorText: String){
        //Toast.makeText(context,errorText,Toast.LENGTH_LONG)
        sth_error_text.text=errorText
        sth_error_text.visibility=View.VISIBLE
        tab_host.visibility=View.GONE
        hideProgress()
    }

    private fun layoutTabs(trainList:List<UiTrainDataGroup>){

        val titles= mutableListOf<String>()
        for(trainDataGroup in trainList){
            titles.add(trainDataGroup.direction)
        }
        setupAdapter(titles,trainList)
        hideProgress()
    }

    private fun showProgress(){
        sth_progressBar.visibility= View.VISIBLE
        ViewCompat.setTranslationZ(sth_progressBar, 2F)
    }

    private fun hideProgress(){
        sth_progressBar.visibility= View.INVISIBLE
    }

    private fun reload(){
        showProgress()
        viewModel.reload()
    }

    private fun setupAdapter(titles:List<String>,trainList:List<UiTrainDataGroup>){
        val trainsPagerAdapter =
            TrainsPagerAdapter(this.childFragmentManager, this.lifecycle,trainList)
        trains_pager.adapter=trainsPagerAdapter
        TabLayoutMediator(trains_tab_layout, trains_pager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    class TrainsPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle,
                             private val trainList:List<UiTrainDataGroup>): FragmentStateAdapter(
        fragmentManager,
        lifecycle
    ) {
        override fun getItemCount(): Int {
            return trainList.size
        }

        override fun createFragment(position: Int): Fragment {
            return ScheduledTrainsFragment.newInstance(trainList[position])
        }
    }
}