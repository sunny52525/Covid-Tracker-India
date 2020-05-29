package com.shaun.covidtrackerindia


import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class DistrictRecyclerViewAdapter(view: View) : RecyclerView.ViewHolder(view) {

    val districtDetails: TextView = view.findViewById(R.id.district_name)
    val confirmedDistric: TextView = view.findViewById(R.id.confirmed_district)
    val zone: TextView = view.findViewById(R.id.zone_district)
    val zonetitle: TextView = view.findViewById(R.id.zone_title)
    val confirmercasestitle: TextView = view.findViewById(R.id.confirmed_title)

}


class RecyclerViewAdapterDistrict(private var covidDistrict: List<districtDetails>) :
    RecyclerView.Adapter<DistrictRecyclerViewAdapter>() {
    private val tag = "RecyclerViewAdapt"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DistrictRecyclerViewAdapter {
        Log.d(tag, "onCreateViewHolder new view requested")
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.district_data, parent, false)
        return DistrictRecyclerViewAdapter(view)
    }

    override fun getItemCount(): Int {
        Log.d(tag, ".getItemCount called")
        return if (covidDistrict.isNotEmpty()) covidDistrict.size else 1
    }

    fun loadNewData(newdata: List<districtDetails>) {
        covidDistrict = newdata
        notifyDataSetChanged()
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DistrictRecyclerViewAdapter, position: Int) {

        if (covidDistrict.isEmpty()) {
            holder.districtDetails.text = "No cases in this State/Territory"

            holder.confirmedDistric.text = ""
            holder.zone.text = ""
            holder.confirmercasestitle.text = ""
            holder.zonetitle.text = ""

        } else {

            val disctrictCovid = covidDistrict[position]
            holder.districtDetails.text = disctrictCovid.district
            holder.confirmedDistric.text = "${disctrictCovid.confirmed_district}"
            holder.zone.text = disctrictCovid.zone
            holder.confirmercasestitle.text = "Confirmed Cases"
            holder.zonetitle.text = "Zone"


        }
    }
}