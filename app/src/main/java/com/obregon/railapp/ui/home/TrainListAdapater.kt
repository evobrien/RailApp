package com.obregon.railapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.obregon.railapp.R

class TrainListAdapter(private val trainList:List<UiTrainData>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
        private val tvRoute:TextView=itemView.findViewById(R.id.route)
        private val tvDestTime:TextView=itemView.findViewById(R.id.dest_time)
        private val tvStatus:TextView=itemView.findViewById(R.id.status_message)
        private val tvDue:TextView=itemView.findViewById(R.id.due)
        private val routeSection=itemView.findViewById<ViewGroup>(R.id.route_section)
        private val tvTrainType:TextView=itemView.findViewById(R.id.train_type)

        fun bind(trainData:UiTrainData){
            tvTrainId.text=trainData.trainCode
            tvRoute.text=trainData.route
            tvDestTime.text=trainData.destinationTime
            tvStatus.text=trainData.status
            tvDue.text=trainData.dueIn
            tvTrainType.text=trainData.trainType
            if(trainData.late){
                routeSection.setBackgroundColor(ContextCompat.getColor(itemView.context,R.color.orange))
            }else{
                routeSection.setBackgroundColor(ContextCompat.getColor(itemView.context,R.color.green))
            }
        }
    }
}