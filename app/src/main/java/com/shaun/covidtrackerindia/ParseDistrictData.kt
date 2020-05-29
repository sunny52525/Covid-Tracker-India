package com.shaun.covidtrackerindia

import android.os.AsyncTask
import android.util.Log
import org.json.JSONArray
import org.json.JSONException


class ParseDistrictData(private val listener: OnDataAvailable) :
    AsyncTask<String, Void, ArrayList<districtDetails>>() {
    private val tag = "ParseDistrictData"

    interface OnDataAvailable {
        fun OnDataAvailable(data: List<districtDetails>)
        fun onError(exception: Exception)
    }

    override fun doInBackground(vararg params: String?): ArrayList<districtDetails> {
        Log.d(tag, "doInBackground Starts")
        val statedetail = ArrayList<districtDetails>()
        try {
            val itemsArray = JSONArray(params[0])
            for (i in 0 until itemsArray.length()) {
                val jsoncontestDetails = itemsArray.getJSONObject(i)
                val district = jsoncontestDetails.getString("name")
                val confirmed = jsoncontestDetails.getLong("confirmed")
                val zone = jsoncontestDetails.getString("zone")

                val districtobject = districtDetails(district, confirmed, zone)
                statedetail.add(districtobject)
                Log.d(tag, "doInBackground $districtobject")
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            Log.d(tag, "doInBackground Error  ${e.message}")
            listener.onError(e)

        }
        Log.d(tag, "doInBackground Ends")
        return statedetail
    }

    override fun onPostExecute(result: ArrayList<districtDetails>) {
        Log.d(tag, "onPostExecute stareted")
        super.onPostExecute(result)
        listener.OnDataAvailable(result)
        Log.d(tag, "onPostExecute ends")
    }
}