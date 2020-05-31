package com.shaun.covidtrackerindia

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*


private var DownloadedJson = "" //I dont know how to implement viewModel as of now
private const val TAG: String = "Main Activity"
private const val splashTimeOut: Long = 3000 // 1 sec

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), GetRawData.OndownloadComplete,
    getCovidJSONData.OnDataAvailable, RecyclerItemClickListener.OnRecyclerClickListener {
    private var aboutDialog: AlertDialog? = null
    private var totalCasesIndia = 0
    private val recyclerViewAdapter = RecyclerViewAdapter(ArrayList())
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.AppTheme2)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        about.setOnClickListener {
            showAbout()
        }

        Log.d(TAG, "OnCreate Called")

        val fileName = "offlinejson.txt"

        val inputString: String = assets.open(fileName).bufferedReader().use { it.readText() }

        if (DownloadedJson == "") {
            onDownloadComplete(inputString, DownloadStatus.OK)

            val getRawData = GetRawData(this)
            getRawData.downloadrawdata("https://api.covidindiatracker.com/state_data.json")    //get raw data in background thread

        } else {

            onDownloadComplete(DownloadedJson, DownloadStatus.OK)
        }

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addOnItemTouchListener(RecyclerItemClickListener(this, recycler_view, this))

        recycler_view.adapter = recyclerViewAdapter

        animate(1F, 0F)
        Handler().postDelayed({
            animate(0F, 1F)
            textView6.text = "Total Cases in India is $totalCasesIndia"


        }, splashTimeOut)

        Log.d(TAG, "ONcreate ENds")
    }

    override fun onDownloadComplete(data: String, status: DownloadStatus) {
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "onDownloadComplete Called")
            DownloadedJson = data
            val getcovidJsonData = getCovidJSONData(this)

            getcovidJsonData.parseJson(data)
        } else {
            Log.d(TAG, "onDownloadCompleted failed with status $status . Error msg is $data")
            Toast.makeText(
                this,
                "Download Error, Make sure Internet is working fine and restart the app \nLast Updated 26/5/2020",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun OnDataAvailable(data: List<StateData>, totalCases: Int) {
        Log.d(TAG, ".OndataAvailable Datta is $data")
        totalCasesIndia = totalCases
        recyclerViewAdapter.loadNewData(data)
        Log.d(TAG, ".OndataAvailable ends")
    }


    override fun onError(exception: Exception) {
        Log.e(TAG, "on error wth $exception")
    }

    override fun onItemClick(view: View, postion: Int) {
        vibrate(10L)
        val district = recyclerViewAdapter.getCovid(postion)
        val intent = Intent(this, districtMainActivity::class.java)
        intent.putExtra("DISTRICTDETAILS", district.toString())
        startActivity(intent)

        Log.d(TAG, "dis ${district.toString()}")
    }

    override fun onItemLongClick(view: View, postion: Int) {
        vibrate(30L)
        Toast.makeText(this, "Click to view District details", Toast.LENGTH_SHORT).show()
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

    private fun animate(start: Float, end: Float) {
        val valueAnimator =
            ValueAnimator.ofFloat(start, end)
        valueAnimator.duration = 5000
        valueAnimator.addUpdateListener { animation ->
            val alpha = animation.animatedValue as Float
            textView6.alpha = alpha
        }
        valueAnimator.start()

    }

    private fun vibrate(sec: Long) {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) { // Vibrator availability checking
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        sec,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                ) // New vibrate method for API Level 26 or higher
            } else {
                vibrator.vibrate(sec) // Vibrate method for below API Level 26
            }
        }
    }

}



