package com.obregon.railapp.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.obregon.railapp.R

class StationAdapter (private var stations:MutableSet<String> ,private var cellClickListener: CellClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

   interface CellClickListener {
       fun onCellClickListener(station: String)
   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RowHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.station_row,parent,false),cellClickListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RowHolder).bind(stations.elementAt(position))
    }

    override fun getItemCount(): Int {
        return stations.size
    }

    fun addItem(item:String){
        if(stations.add(item)){
            this.notifyItemInserted(stations.size-1)
            notifyDataSetChanged()
        }
    }

    class RowHolder (itemView: View, private val cellClickListener: CellClickListener):RecyclerView.ViewHolder(itemView){
        private var tvStation: TextView=itemView.findViewById(R.id.tv_name)

        fun bind( station:String){
            tvStation.text=station
            itemView.setOnClickListener {
                cellClickListener.onCellClickListener(station)
            }
        }
    }

}


