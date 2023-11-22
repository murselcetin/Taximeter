package com.morpion.taximeter.presentation.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.morpion.taximeter.R

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<viewBinding : ViewBinding>(private val inflate: Inflate<viewBinding>) :
    Fragment() {

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
                        .setTitle("Taksi+")
                        .setMessage("Uygumaladan çıkmak istediğinize emin misiniz?")
                        .setIcon(R.drawable.ic_logo)
                        .setPositiveButton("Evet") { d, _ ->
                            activity?.finish()
                            d.dismiss()
                        }
                        .setNegativeButton("Hayır") { d, _ -> d.dismiss() }
                        .create()
                    dialog.show()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

    fun alertDialog(text: String, type: Int) {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val alertDesign = LayoutInflater.from(context).inflate(R.layout.not_completed_dialog, null)
        val ivDialog = alertDesign.findViewById(R.id.iv_dialog) as ImageView
        when (type) {
            0 -> {
                ivDialog.setImageResource(R.drawable.ic_error)
            }
            1 -> {
                ivDialog.setImageResource(R.drawable.ic_success)
            }
            else ->{}
        }
        val errorText = alertDesign.findViewById(R.id.tv_error_text) as TextView
        errorText.text = text
        builder.setView(alertDesign)
        val d = builder.create()
        d.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        d.show()
        d.setCanceledOnTouchOutside(false)
        Handler(Looper.getMainLooper()).postDelayed({
            d.dismiss()
        }, 2500)
    }

}
