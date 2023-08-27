package com.morpion.taximeter.shared

import com.google.gson.Gson
import com.morpion.taximeter.domain.model.TaxiFaresData
import com.morpion.taximeter.util.LocalSessions
import javax.inject.Inject

class TaxiFaresManager @Inject constructor(private val sessions: LocalSessions) {

    var lastTaxiFaresList: List<TaxiFaresData> = emptyList()

    private var isLocalUpdate: Boolean = false

    fun getTaxiFares(): List<TaxiFaresData> {
        var list = sortTaxiFare()
        if (list.isEmpty()) {
            if (!isLocalUpdate) {
                val localTaxiFare = sessions.taxiFaresList
                if (localTaxiFare != null) {
                    try {
                        val newLocal = Gson().fromJson(localTaxiFare, Array<TaxiFaresData>::class.java)
                            .toList() as ArrayList<TaxiFaresData>
                        lastTaxiFaresList = newLocal
                        isLocalUpdate = true
                        list = sortTaxiFare()
                    } catch (e: Exception) {
                    }
                }
            }
        }
        return list
    }

    private fun sortTaxiFare(): List<TaxiFaresData> {
        return lastTaxiFaresList.sortedBy { it.id }
    }

    fun setTaxiFareList(TaxiFare: List<TaxiFaresData>) {
        lastTaxiFaresList = TaxiFare
        saveTaxiFare(TaxiFare)
    }

    fun searchCityTaxiFares(city: String): TaxiFaresData? {
        val data = lastTaxiFaresList.single { s -> s.city == city}
        data.feePerKm?.let {
            return data
        }
        return null
    }

    private fun saveTaxiFare(TaxiFare: List<TaxiFaresData>) {
        sessions.taxiFaresList = Gson().toJson(TaxiFare).toString()
    }
}