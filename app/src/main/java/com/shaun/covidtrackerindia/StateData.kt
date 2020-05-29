package com.shaun.covidtrackerindia

import org.json.JSONArray

data class StateData(
    var StateName: String,
    val Deaths: Long,
    val ConfirmedCases: Long,
    val ActiveCases: Long,
    val Recovered: Long,
    val district: JSONArray
) {
    override fun toString(): String {
        return "State is $StateName , Confirmed Cases  are $ConfirmedCases , Total Deaths $Deaths ,Active Cases $ActiveCases , Recovered $Recovered "
    }
}