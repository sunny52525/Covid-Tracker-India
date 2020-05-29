package com.shaun.covidtrackerindia

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.district_activity_main.*
import org.json.JSONArray
import org.json.JSONException


class districtMainActivity : AppCompatActivity(), ParseDistrictData.OnDataAvailable {
    private val tag = "district main activity"
    private var aboutDialog: AlertDialog? = null
    private val recyclerViewAdapter = RecyclerViewAdapterDistrict(ArrayList())
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme2)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.district_activity_main)
        about.setOnClickListener {
            showAbout()
        }
        fuckthisshit.layoutManager = LinearLayoutManager(this)
        fuckthisshit.adapter = recyclerViewAdapter
        val jsonArray = intent.getStringExtra("DISTRICTDETAILS")

        try {
            val array = JSONArray(jsonArray)
            println(array.toString(2))
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val districtdate = ParseDistrictData(this)
        districtdate.execute(jsonArray)

        Log.d(tag, "DISTRICT DETAILS $jsonArray")


    }

    override fun OnDataAvailable(data: List<districtDetails>) {
        recyclerViewAdapter.loadNewData(data)
    }

    override fun onError(exception: Exception) {
        Log.d(tag, "fuck")
    }

    @SuppressLint("InflateParams")
    private fun showAbout() {
        val messgView = layoutInflater.inflate(R.layout.about, null, false)
        val builder = AlertDialog.Builder(this)

        builder.setTitle(R.string.app_name)
        builder.setIcon(R.mipmap.ic_launcher)
        aboutDialog = builder.setView(messgView).create()
        aboutDialog?.setCanceledOnTouchOutside(true)
        aboutDialog?.show()
    }
}