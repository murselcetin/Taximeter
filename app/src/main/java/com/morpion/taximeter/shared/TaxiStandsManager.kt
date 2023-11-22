package com.morpion.taximeter.shared

import com.google.gson.Gson
import com.morpion.taximeter.domain.model.TaxiStandsData
import com.morpion.taximeter.util.LocalSessions
import javax.inject.Inject

class TaxiStandsManager @Inject constructor(private val sessions: LocalSessions) {

    var lastTaxiStandsList: List<TaxiStandsData> = emptyList()

    private var isLocalUpdate: Boolean = false

    fun getTaxiStands(): List<TaxiStandsData> {
        var list = sortTaxiStand()
        if (list.isEmpty()) {
            if (!isLocalUpdate) {
                val localTaxiStand = sessions.taxiStandsList
                if (localTaxiStand != null) {
                    try {
                        val newLocal = Gson().fromJson(localTaxiStand, Array<TaxiStandsData>::class.java)
                            .toList() as ArrayList<TaxiStandsData>
                        lastTaxiStandsList = newLocal
                        isLocalUpdate = true
                        list = sortTaxiStand()
                    } catch (e: Exception) {
                    }
                }
            }
        }
        return list
    }

    private fun sortTaxiStand(): List<TaxiStandsData> {
        return lastTaxiStandsList.sortedBy { it.id }
    }

    fun setTaxiStandList(TaxiStand: List<TaxiStandsData>) {
        lastTaxiStandsList = TaxiStand
        saveTaxiStand(TaxiStand)
    }

    private fun saveTaxiStand(TaxiStand: List<TaxiStandsData>) {
        sessions.taxiStandsList = Gson().toJson(TaxiStand).toString()
    }
}