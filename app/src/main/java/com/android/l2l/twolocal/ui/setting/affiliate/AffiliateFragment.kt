package com.android.l2l.twolocal.ui.setting.affiliate

import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.common.findAppComponent
import com.android.l2l.twolocal.common.onMessageToast
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.databinding.FragmentAffiliateBinding
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.ui.base.BaseFragment
import com.android.l2l.twolocal.ui.setting.di.DaggerSettingComponent
import com.android.l2l.twolocal.utils.CommonUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AffiliateFragment : BaseFragment<AffiliateViewModel>(R.layout.fragment_affiliate) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    override val viewModel: AffiliateViewModel by viewModels { viewModelFactory }
    private val binding: FragmentAffiliateBinding by viewBinding(FragmentAffiliateBinding::bind)
    var link: String? = null

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        DaggerSettingComponent.factory().create(requireActivity().findAppComponent()).inject(this)
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMyPreferLink()

        viewModel.preferLinkLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Success -> {
                    link = it.response
                    binding.textReferralLink2local.text = it.response
                }
                is ViewState.Error -> {
                    onMessageToast(it.error?.message)
                }
                else -> {
                }
            }
        })

        binding.buttonCopy.setOnClickListener {
            link?.let {
                CommonUtils.copyToClipboard(requireContext(), link)
            }
        }

        binding.btnShare.setOnClickListener {
            CommonUtils.shareText(requireContext(), link,getString(R.string.affiliate_share_title))
        }

        binding.toolbar.getBackBtn().setOnClickListener {
            findNavController().popBackStack()
        }
    }


}