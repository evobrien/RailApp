package com.obregon.railapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.obregon.railapp.R

class TrainListAdapter(private val trainList:List<UiTrainListData>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView=inflater.inflate(R.layout.train_list_item, parent,false)
        return TrainItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TrainItemViewHolder).bind(trainList[position])
    }

    override fun getItemCount(): Int {
       return trainList.size
    }

    class TrainItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        private val tvTrainId:TextView=itemView.findViewById(R.id.train_id)
        private val tvDirection:TextView=itemView.findViewById(R.id.direction)
        private val tvOrigin:TextView=itemView.findViewById(R.id.origin)
        private val tvDestination:TextView=itemView.findViewById(R.id.destination)
        private val tvOriginTime:TextView=itemView.findViewById(R.id.origin_time)
        private val tvDestTime:TextView=itemView.findViewById(R.id.dest_time)
        private val tvStatus:TextView=itemView.findViewById(R.id.status_message)
        private val tvLateYn:TextView=itemView.findViewById(R.id.late_yn)
        private val tvDue:TextView=itemView.findViewById(R.id.due)



        fun bind(trainData:UiTrainListData){
            tvTrainId.text=trainData.uiTrainData.trainCode
            tvDirection.text=trainData.uiTrainData.direction
            tvOrigin.text=trainData.uiTrainData.origin
            tvDestination.text=trainData.uiTrainData.destination
            tvOriginTime.text=trainData.uiTrainData.originTime
            tvDestTime.text=trainData.uiTrainData.destinationTime
            tvStatus.text=trainData.uiTrainData.status
            tvLateYn.text=trainData.uiTrainData.late
            tvDue.text=trainData.uiTrainData.dueIn
        }
    }
}