package com.shaun.covidtrackerindia

import android.os.AsyncTask
import android.util.Log
import org.json.JSONArray
import org.json.JSONException


class getCovidJSONData(private val listener: OnDataAvailable) :
    AsyncTask<String, Void, ArrayList<StateData>>() {
    private val tag = "getCovidJSONData"
    private var total=0
    interface OnDataAvailable {
        fun OnDataAvailable(data: List<StateData>,totalCases :Int)
        fun onError(exception: Exception)
    }

    override fun doInBackground(vararg params: String?): ArrayList<StateData> {
        Log.d(tag, "doInBackground Starts")
        val contestDetails = ArrayList<StateData>()
        try {
            val itemsArray = JSONArray(params[0])
            for (i in 0 until itemsArray.length())
            {
                val jsoncontestDetails = itemsArray.getJSONObject(i)
                val state = jsoncontestDetails.getString("state")
                val active = jsoncontestDetails.getLong("active")
                val confirmed = jsoncontestDetails.getLong("confirmed")
                total += confirmed.toInt()
                val death = jsoncontestDetails.getLong("deaths")
                val recovered = jsoncontestDetails.getLong("recovered")
                val district = jsoncontestDetails.getJSONArray("districtData")

                val contestObject = StateData(state, death, confirmed, active, recovered, district)
                contestDetails.add(contestObject)
                Log.d(tag, "doInBackground $contestObject")
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            Log.d(tag, "doInBackground Error  ${e.message}")
            listener.onError(e)

        }
        Log.d(tag, "doInBackground Ends")
        return contestDetails
    }

    override fun onPostExecute(result: ArrayList<StateData>) {
        Log.d(tag, "onPostExecute stareted")
        super.onPostExecute(result)
        listener.OnDataAvailable(result,total)
        Log.d(tag, "onPostExecute ends")
    }
}