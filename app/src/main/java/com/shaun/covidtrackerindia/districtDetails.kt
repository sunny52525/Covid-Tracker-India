package com.shaun.covidtrackerindia

data class districtDetails(
    var district: String,
    var confirmed_district: Long,
    var zone: String

) {
    override fun toString(): String {
        return "{District is $district , Confirmed cases $confirmed_district  and zone is $zone)"
    }
}