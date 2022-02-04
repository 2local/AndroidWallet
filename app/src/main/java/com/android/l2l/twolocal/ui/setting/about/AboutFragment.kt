package com.android.l2l.twolocal.ui.setting.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.common.findAppComponent
import com.android.l2l.twolocal.databinding.FragmentAboutBinding
import com.android.l2l.twolocal.di.component.AppComponent
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.ui.base.BaseFragment
import com.android.l2l.twolocal.ui.base.BaseViewModel
import com.android.l2l.twolocal.ui.setting.di.DaggerSettingComponent
import com.android.l2l.twolocal.utils.CommonUtils
import com.android.l2l.twolocal.utils.constants.AppConstants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AboutFragment : BaseFragment<BaseViewModel>(R.layout.fragment_about) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    override val viewModel: BaseViewModel by viewModels { viewModelFactory }
    private val binding: FragmentAboutBinding by viewBinding(FragmentAboutBinding::bind)

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        DaggerSettingComponent.factory().create(requireActivity().findAppComponent()).inject(this)
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val versionName = CommonUtils.getAppVersion(requireContext())
        binding.itemVersionName.text = getString(R.string.fragment_about_version_prefix, versionName)

        binding.privacyPolicy.setOnClickListener {
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(AppConstants.PRIVACY_POLICY)
                requireActivity().startActivity(this)
            }

        }

        binding.feedback.setOnClickListener {
            CommonUtils.openPlayStoreForApp(requireContext())
        }

        binding.toolbar.getBackBtn().setOnClickListener {
            findNavController().popBackStack()
        }

        binding.termsOfConditions.setOnClickListener {
            Intent(context, WebViewActivity::class.java).apply {
                requireContext().startActivity(this)

            }
        }
    }


}