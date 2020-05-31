package com.shaun.covidtrackerindia


import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

enum class DownloadStatus {
    OK, IDLE, NOT_INITIALISED, FAILED_OR_EMPTY, PERMISSION_ERROR, ERROR
}

class GetRawData(private val listener: OndownloadComplete)  {
    private val tag = "GetRawData"
    private var downloadStatus = DownloadStatus.IDLE

    interface OndownloadComplete {
        fun onDownloadComplete(data: String, status: DownloadStatus)
    }

     fun AfterDownloading(result: String) {
        Log.d(tag, "onPOst Ex wiht val $result")
        listener.onDownloadComplete(result, downloadStatus)
    }

     fun InBackGroundThread(vararg params: String?): String {
        Log.d(tag, "DoInBackground Starts")
        if (params[0] == null) {
            downloadStatus = DownloadStatus.NOT_INITIALISED
            return "no URL specified"
        }
        try {

            downloadStatus = DownloadStatus.OK
            val data: String? = URL(params[0]).readText()
            Log.d(tag, data!!)
            return data
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is MalformedURLException -> {
                    downloadStatus = DownloadStatus.NOT_INITIALISED
                    "doInBackground:Invalid URL ${e.message}"
                }
                is IOException -> {
                    downloadStatus = DownloadStatus.FAILED_OR_EMPTY
                    "doInBackground:IO Exception ${e.message}"
                }
                is SecurityException -> {
                    downloadStatus = DownloadStatus.PERMISSION_ERROR
                    "doInBackground:Security Exception : Give the Goddamn permission ${e.message}"
                }
                else
                -> {

                    downloadStatus = DownloadStatus.ERROR
                    "Unkown Error : ${e.message}"

                }
            }
            Log.e(tag, errorMessage)
            return errorMessage
        }
    }
    fun downloadrawdata( link : String){
        GlobalScope.launch {
            val result=InBackGroundThread(link)
            withContext(Dispatchers.Main){
                AfterDownloading(result)
            }
        }
    }

}