package com.android.l2l.twolocal.ui.setting.currency

import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.common.findAppComponent
import com.android.l2l.twolocal.common.onMessageToast
import com.android.l2l.twolocal.common.onSuccessDialog
import com.android.l2l.twolocal.common.setBackStackData
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.databinding.FragmentCurrencyBinding
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.model.enums.FiatType
import com.android.l2l.twolocal.model.event.RefreshWalletListEvent
import com.android.l2l.twolocal.ui.base.BaseFragment
import com.android.l2l.twolocal.ui.setting.SettingFragment
import com.android.l2l.twolocal.ui.setting.di.DaggerSettingComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject


@ExperimentalCoroutinesApi
class CurrencyFragment : BaseFragment<CurrencyViewModel>(R.layout.fragment_currency) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    override val viewModel: CurrencyViewModel by viewModels { viewModelFactory }
    private val binding: FragmentCurrencyBinding by viewBinding(FragmentCurrencyBinding::bind)

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        DaggerSettingComponent.factory().create(requireActivity().findAppComponent()).inject(this)
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.updateCurrencyLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Success -> {
                    onSuccessDialog(getString(R.string.your_currency_changed_to_, it.response.myName))
                    EventBus.getDefault().post(RefreshWalletListEvent())
                    setBackStackData(SettingFragment.CURRENCY_KEY, it.response)
                }
                is ViewState.Error -> {
                    onMessageToast(it.error.message)
                }
                else -> {
                }
            }
        })

        binding.dollarContainer.setOnClickListener {
            viewModel.saveCurrentCurrency(FiatType.USD)
        }

        binding.euroContainer.setOnClickListener {
            viewModel.saveCurrentCurrency(FiatType.EUR)
        }

        binding.toolbar.getBackBtn().setOnClickListener {
            findNavController().popBackStack()
        }

    }


}