package com.morpion.taximeter.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.morpion.taximeter.R
import com.morpion.taximeter.common.extensions.setSafeOnClickListener
import com.morpion.taximeter.common.extensions.timestampToDate
import com.morpion.taximeter.databinding.RecyclerTaximeterHistoryItemBinding
import com.morpion.taximeter.domain.model.ui.TaximeterHistoryUIModel

class TaximeterHistoryAdapter(private val onItemClick: (ClickEvent,TaximeterHistoryUIModel) -> Unit) : ListAdapter<TaximeterHistoryUIModel, TaximeterViewHolder>(DiffCallbackTaximeter()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaximeterViewHolder {
        val binding = RecyclerTaximeterHistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaximeterViewHolder(binding,onItemClick)
    }

    override fun onBindViewHolder(holder: TaximeterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    enum class ClickEvent {
        DELETE,
        COPY,
        EXPANDED
    }
}

private class DiffCallbackTaximeter : DiffUtil.ItemCallback<TaximeterHistoryUIModel>() {
    override fun areItemsTheSame(oldItem: TaximeterHistoryUIModel, newItem: TaximeterHistoryUIModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TaximeterHistoryUIModel, newItem: TaximeterHistoryUIModel): Boolean {
        return oldItem.id == newItem.id
    }
}

class TaximeterViewHolder(private val binding: RecyclerTaximeterHistoryItemBinding, private val onItemClick: (TaximeterHistoryAdapter.ClickEvent, TaximeterHistoryUIModel) -> Unit) : RecyclerView.ViewHolder(binding.root) {
    fun bind(taximeterData: TaximeterHistoryUIModel?) {
        taximeterData?.let { itTaximeterData ->
            binding.tvPaid.text = itTaximeterData.paid
            binding.tvDistance.text = itTaximeterData.distance
            binding.tvDuration.text = itTaximeterData.time
            binding.tvDate.text = itTaximeterData.date.timestampToDate()
            binding.ivMap.setImageBitmap(itTaximeterData.img)
        }
        binding.ivMap.setSafeOnClickListener {
            taximeterData?.let { itData ->
                onItemClick.invoke(TaximeterHistoryAdapter.ClickEvent.EXPANDED,itData)
            }
        }
        binding.ivPopup.setSafeOnClickListener {
            val popupMenu = PopupMenu(binding.ivPopup.context, binding.ivPopup)
            popupMenu.inflate(R.menu.popup_menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.popup_delete -> {
                        taximeterData?.let { itData ->
                            onItemClick.invoke(TaximeterHistoryAdapter.ClickEvent.DELETE,itData)
                        }
                    }
                    R.id.popup_copy -> {
                        taximeterData?.let { itData ->
                            onItemClick.invoke(TaximeterHistoryAdapter.ClickEvent.COPY,itData)
                        }
                    }
                    else -> Unit
                }
                false
            }
            popupMenu.show()
        }
    }
}