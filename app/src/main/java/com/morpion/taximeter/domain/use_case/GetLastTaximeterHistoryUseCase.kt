package com.morpion.taximeter.domain.use_case

import com.morpion.taximeter.domain.repository.LocalRepository
import javax.inject.Inject

class GetLastTaximeterHistoryUseCase @Inject constructor(private val repository: LocalRepository) {

     fun getLastTaximeterHistory() = repository.getLastTaximeterHistory()

}