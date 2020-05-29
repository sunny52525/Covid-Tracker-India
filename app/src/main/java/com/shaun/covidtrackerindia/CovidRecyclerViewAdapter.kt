package com.shaun.covidtrackerindia


import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray


class CovidRecyclerViewAdapter(view: View) : RecyclerView.ViewHolder(view) {
    var stateName: TextView = view.findViewById(R.id.state_name)
    var activeCases: TextView = view.findViewById(R.id.active_cases)
    var deaths: TextView = view.findViewById(R.id.deaths)
    var recovered: TextView = view.findViewById(R.id.recovered_cases)
    var total: TextView = view.findViewById(R.id.confirmed_cases)
    val dummy1: TextView = view.findViewById(R.id.textView10)
    val dummy2: TextView = view.findViewById(R.id.textView11)
    val dummy3: TextView = view.findViewById(R.id.textView14)
    val dummy4: TextView = view.findViewById(R.id.textView16)

}


class RecyclerViewAdapter(private var covid: List<StateData>) :
    RecyclerView.Adapter<CovidRecyclerViewAdapter>() {
    private val tag = "RecyclerViewAdapt"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CovidRecyclerViewAdapter {
        Log.d(tag, "onCreateViewHolder new view requested")
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.statedata, parent, false)
        return CovidRecyclerViewAdapter(view)
    }

    override fun getItemCount(): Int {
        Log.d(tag, ".getItemCount called")
        return if (covid.isNotEmpty()) covid.size else 1
    }

    fun loadNewData(newContest: List<StateData>) {
        covid = newContest
        notifyDataSetChanged()
    }

    fun getCovid(position: Int): JSONArray? {
        return if (covid.isNotEmpty()) covid[position].district else null
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CovidRecyclerViewAdapter, position: Int) {
        //called by layout manager (needs existing view)

        if (covid.isEmpty()) {
            holder.stateName.text = "No or Slow Internet Connection "
            holder.activeCases.text = ""
            holder.deaths.text = ""
            holder.recovered.text = ""
            holder.total.text = ""
            holder.dummy1.text = ""
            holder.dummy2.text = ""
            holder.dummy4.text = ""
            holder.dummy3.text = ""
        } else {

            val stateCovid = covid[position]

            holder.stateName.text = stateCovid.StateName
            holder.activeCases.text = "${stateCovid.ActiveCases}"
            holder.deaths.text = "${stateCovid.Deaths}"
            holder.recovered.text = "${stateCovid.Recovered}"
            holder.total.text = "${stateCovid.ConfirmedCases}"
            holder.dummy1.text = "Active Cases"
            holder.dummy2.text = "Confirmed Cases"
            holder.dummy3.text = "Recovered"
            holder.dummy4.text = "Total Deaths"


        }


    }
}