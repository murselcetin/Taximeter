package com.morpion.taximeter.presentation.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.morpion.taximeter.R

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<viewBinding : ViewBinding>(private val inflate: Inflate<viewBinding>) : Fragment() {

    private lateinit var _binding: viewBinding
    protected val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val dialog = MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Taksimetre")
                        .setMessage("Uygumaladan çıkmak istediğinize emin misiniz?")
                        .setIcon(R.drawable.ic_taxi)
                        .setPositiveButton("Evet") { d,_ ->
                            activity?.finish()
                            d.dismiss()
                        }
                        .setNegativeButton("Hayır") { d,_ -> d.dismiss() }
                        .create()

                    dialog.show()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }
}
