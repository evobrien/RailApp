package com.obregon.railapp.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.obregon.railapp.R
import com.obregon.railapp.ui.search.MapSearchScreen
import com.obregon.railapp.ui.search.TextSearchScreen
import kotlinx.android.synthetic.main.scheduled_screen_fragment.*

const val TRAIN_LIST="TRAIN_LIST"
class ScheduledTrainsFragment:Fragment(R.layout.scheduled_screen_fragment) {

    companion object{
        fun newInstance(trainDataGroup:UiTrainDataGroup):ScheduledTrainsFragment {
            val args = Bundle()
            args.putParcelable(TRAIN_LIST,trainDataGroup)
            val fragment = ScheduledTrainsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val trainDataList=arguments?.getParcelable<UiTrainDataGroup>(TRAIN_LIST)
        trainDataList?.let {
            layoutList(it.uiTrainDataList)
        }
    }

    private fun layoutList(trainList:List<UiTrainData>){
        error_text.visibility= View.GONE
        train_list.visibility= View.VISIBLE

        train_list.apply {
            adapter=TrainListAdapter(trainList)
            layoutManager= LinearLayoutManager(context)
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

}