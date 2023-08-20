package com.morpion.taximeter.presentation.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.morpion.taximeter.common.extensions.setSafeOnClickListener
import com.morpion.taximeter.common.extensions.timestampToDate
import com.morpion.taximeter.data.local.entity.toUiModel
import com.morpion.taximeter.databinding.FragmentTaximeterHistoryBinding
import com.morpion.taximeter.domain.model.ui.TaximeterHistoryUIModel
import com.morpion.taximeter.presentation.base.BaseFragment
import com.morpion.taximeter.presentation.ui.adapter.TaximeterHistoryAdapter
import com.morpion.taximeter.presentation.ui.viewmodel.TaximeterHistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TaximeterHistoryFragment : BaseFragment<FragmentTaximeterHistoryBinding>(FragmentTaximeterHistoryBinding::inflate) {

    private val viewModel: TaximeterHistoryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe()
        setupTaximeterHistoryAdapter()

        viewModel.getTaximeterHistory()

    }

    private val recyclerTaximeterHistoryAdapter = TaximeterHistoryAdapter() { event, data ->
        when (event) {
            TaximeterHistoryAdapter.ClickEvent.DELETE -> {
                deleteTaximeter(data)
            }
            TaximeterHistoryAdapter.ClickEvent.COPY -> {
                copyData(data)
            }
            TaximeterHistoryAdapter.ClickEvent.EXPANDED -> {
                zoomImageFromThumb(binding.rvTaximeterHistory, data.img)
            }
        }
    }

    private fun setupTaximeterHistoryAdapter() {
        binding.rvTaximeterHistory.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvTaximeterHistory.adapter = recyclerTaximeterHistoryAdapter
    }

    private fun observe() = with(viewModel) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    uiState.collectLatest { uiState ->
                        val lastTaximeterHistory = uiState.data
                        val lastTaximeterHistoryList = ArrayList<TaximeterHistoryUIModel>()
                        lastTaximeterHistory.forEach { itTaximeter ->
                            lastTaximeterHistoryList.add(itTaximeter.toUiModel())
                        }
                        if (lastTaximeterHistoryList.isNotEmpty()) {
                            (binding.rvTaximeterHistory.adapter as TaximeterHistoryAdapter).submitList(lastTaximeterHistoryList)
                        } else {
                            (binding.rvTaximeterHistory.adapter as TaximeterHistoryAdapter).submitList(emptyList())
                        }
                    }
                }
            }

        }
    }

    private fun deleteTaximeter(data: TaximeterHistoryUIModel) {
        viewModel.deleteTaximeter(data.id)
    }

    private fun copyData(data: TaximeterHistoryUIModel) {
        val text = "${data.date.timestampToDate()} tarihinde yapılan ${data.distance} km mesafelik yolculuğunuzun ücreti ${data.paid} ₺'dir."
        val clipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clip = ClipData.newPlainText("Taximeter", text)
        clipboard?.setPrimaryClip(clip)
        Toast.makeText(requireContext(), "Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
    }

    private fun navigateHomeFragment(){
        val action = TaximeterHistoryFragmentDirections.actionTaximeterHistoryFragmentToHomeFragment()
        findNavController().navigate(action)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateHomeFragment()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

    private fun zoomImageFromThumb(thumbView: View, image: Bitmap?) {
        binding.ivExpanded.setImageBitmap(image)

        val startBoundsInt = Rect()
        val finalBoundsInt = Rect()
        val globalOffset = Point()

        thumbView.getGlobalVisibleRect(startBoundsInt)
        binding.rvTaximeterHistory.getGlobalVisibleRect(finalBoundsInt, globalOffset)
        startBoundsInt.offset(-globalOffset.x, -globalOffset.y)
        finalBoundsInt.offset(-globalOffset.x, -globalOffset.y)

        val startBounds = RectF(startBoundsInt)
        val finalBounds = RectF(finalBoundsInt)

        val startScale: Float
        if ((finalBounds.width() / finalBounds.height() > startBounds.width() / startBounds.height())) {
            startScale = startBounds.height() / finalBounds.height()
            val startWidth: Float = startScale * finalBounds.width()
            val deltaWidth: Float = (startWidth - startBounds.width()) / 2
            startBounds.left -= deltaWidth.toInt()
            startBounds.right += deltaWidth.toInt()
        } else {
            startScale = startBounds.width() / finalBounds.width()
            val startHeight: Float = startScale * finalBounds.height()
            val deltaHeight: Float = (startHeight - startBounds.height()) / 2f
            startBounds.top -= deltaHeight.toInt()
            startBounds.bottom += deltaHeight.toInt()
        }

        thumbView.alpha = 0f

        binding.ivExpanded.visibility = View.VISIBLE
        binding.ivClose.visibility = View.VISIBLE

        binding.ivClose.setSafeOnClickListener {
            binding.ivExpanded.visibility = View.GONE
            binding.ivClose.visibility = View.GONE
            binding.rvTaximeterHistory.alpha = 1f
        }
    }
}