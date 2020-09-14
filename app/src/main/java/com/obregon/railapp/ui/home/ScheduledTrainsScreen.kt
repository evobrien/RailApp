package com.obregon.railapp.ui.home

import android.os.Bundle
import android.view.*
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.obregon.railapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.scheduled_screen_fragment.*

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
        return inflater.inflate(R.layout.scheduled_screen_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val args=arguments
        val station=args?.getString(STATION_KEY)
        station?.let {
            viewModel.getTrainData(station)
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
        viewModel.uiDataList.observeForever{layoutList(it)}
    }

    private fun handleError(string: String){
        error_text.text=string
        error_text.visibility=View.VISIBLE
        train_list.visibility=View.GONE
        hideProgress()
    }

    private fun layoutList(trainList:List<UiTrainListData>){
        error_text.visibility=View.GONE
        train_list.visibility=View.VISIBLE

        train_list.apply {
            adapter=TrainListAdapter(trainList)
            layoutManager=LinearLayoutManager(context)
        }
        hideProgress()
    }

    private fun showProgress(){
        progressBar.visibility= View.VISIBLE
        ViewCompat.setTranslationZ(progressBar, 2F)
    }

    private fun hideProgress(){
        progressBar.visibility= View.INVISIBLE
    }

    private fun reload(){
        showProgress()
        viewModel.reload()
    }
}