package com.morpion.taximeter.presentation.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.morpion.taximeter.R
import com.morpion.taximeter.common.extensions.setSafeOnClickListener
import com.morpion.taximeter.databinding.FragmentSettingsBinding
import com.morpion.taximeter.presentation.base.BaseFragment
import java.util.*

class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createDateNow()

        binding.clTaximeterPaid.setSafeOnClickListener {
            navigateTaximeterPaidFragment()
        }

        binding.clShare.setSafeOnClickListener {
            shareGooglePlayURL()
        }

        binding.clContact.setSafeOnClickListener {
            navigateInstagramPage()
        }

        binding.clPrivacy.setSafeOnClickListener {
            navigatePrivacyPolicy()
        }
    }

    private fun navigatePrivacyPolicy() {
        val uri = Uri.parse("https://morpionsoftware.com/taximeter/privacy.html")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun navigateInstagramPage() {
        val uri = Uri.parse("https://www.instagram.com/taksimetreplus/")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun shareGooglePlayURL() {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.morpion.taximeter")
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Paylaş")
        startActivity(Intent.createChooser(shareIntent, "Paylaş"))
    }

    private fun navigateTaximeterPaidFragment() {
        val action = SettingsFragmentDirections.actionSettingsFragmentToTaximeterFeeFragment()
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

    private fun navigateHomeFragment() {
        val action = SettingsFragmentDirections.actionSettingsFragmentToHomeFragment()
        findNavController().navigate(action)
    }

    private fun createDateNow() {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        binding.tvCopyright.text = "© $year Morpion Software"
    }
}