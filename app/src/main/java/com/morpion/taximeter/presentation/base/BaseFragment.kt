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
                    val builder = AlertDialog.Builder(context)
                    val alertDesign = LayoutInflater.from(context).inflate(R.layout.app_exit_dialog, null)
                    val yesButton = alertDesign.findViewById(R.id.buttonYes) as Button
                    val noButton = alertDesign.findViewById(R.id.buttonNo) as Button
                    builder.setView(alertDesign)
                    val d = builder.create()
                    d.setCanceledOnTouchOutside(false)
                    d.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    yesButton.setOnClickListener {
                        activity?.finish()
                        d.dismiss()
                    }
                    noButton.setOnClickListener {
                        d.dismiss()
                    }
                    d.show()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }
}
