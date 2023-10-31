package com.morpion.taximeter.presentation.login

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.viewpager2.widget.ViewPager2
import com.morpion.taximeter.common.extensions.safeNavigate
import com.morpion.taximeter.common.extensions.setSafeOnClickListener
import com.morpion.taximeter.databinding.FragmentOnboardingBinding
import com.morpion.taximeter.presentation.base.BaseFragment
import com.morpion.taximeter.util.LocalSessions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingFragment : BaseFragment<FragmentOnboardingBinding>(FragmentOnboardingBinding::inflate) {

    @Inject
    lateinit var localSessions: LocalSessions

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvNextIntro.setSafeOnClickListener {
            binding.viewpager.setCurrentItem(3, true)
        }

        binding.clStartNow.setSafeOnClickListener {
            navigateTaximeterFeePage()
        }

        if (localSessions.firstTutorial) {
            navigateTaximeterFeePage()
        } else {
            setupViewPager()
        }

        binding.viewpager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        binding.tvNextIntro.visibility = View.VISIBLE
                        binding.clStartNow.visibility = View.INVISIBLE
                    }
                    1 -> {
                        binding.tvNextIntro.visibility = View.INVISIBLE
                        binding.clStartNow.visibility = View.INVISIBLE
                    }
                    2 -> {
                        binding.tvNextIntro.visibility = View.INVISIBLE
                        binding.clStartNow.visibility = View.INVISIBLE
                    }
                    3 -> {
                        binding.tvNextIntro.visibility = View.INVISIBLE
                        binding.clStartNow.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun navigateTaximeterFeePage() {
        localSessions.firstTutorial = true
        val action = OnboardingFragmentDirections.actionOnboardingFragmentToFirstTaximeterFeeFragment()
        findNavController().safeNavigate(action)
    }

    private fun setupViewPager() {
        binding.viewpager.adapter = PageAdapter(childFragmentManager)
        binding.viewpager.setPageTransformer(true, CustomPageTransformer())
    }
}