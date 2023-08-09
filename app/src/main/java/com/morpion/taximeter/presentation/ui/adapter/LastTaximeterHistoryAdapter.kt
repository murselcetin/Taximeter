package com.morpion.taximeter.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.morpion.taximeter.common.extensions.timestampToDate
import com.morpion.taximeter.databinding.RecyclerLastTaximeterHistoryItemBinding
import com.morpion.taximeter.domain.model.ui.TaximeterHistoryUIModel

class LastTaximeterHistoryAdapter() : ListAdapter<TaximeterHistoryUIModel, LastTaximeterViewHolder>(DiffCallbackLastTaximeter()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastTaximeterViewHolder {
        val binding = RecyclerLastTaximeterHistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LastTaximeterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LastTaximeterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private class DiffCallbackLastTaximeter : DiffUtil.ItemCallback<TaximeterHistoryUIModel>() {
    override fun areItemsTheSame(oldItem: TaximeterHistoryUIModel, newItem: TaximeterHistoryUIModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TaximeterHistoryUIModel, newItem: TaximeterHistoryUIModel): Boolean {
        return oldItem.id == newItem.id
    }
}

class LastTaximeterViewHolder(private val binding: RecyclerLastTaximeterHistoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(taximeterData: TaximeterHistoryUIModel?) {
        taximeterData?.let { itTaximeterData ->
            binding.tvPaid.text = itTaximeterData.paid
            binding.tvDistance.text = itTaximeterData.distance
            binding.tvDuration.text = itTaximeterData.time
            binding.tvDate.text = itTaximeterData.date.timestampToDate()
        }
    }
}