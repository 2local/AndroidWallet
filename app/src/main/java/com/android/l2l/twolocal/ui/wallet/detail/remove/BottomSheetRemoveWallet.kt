package com.android.l2l.twolocal.ui.wallet.detail.remove

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.common.findAppComponent
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.databinding.BottomsheetRemoveWalletBinding
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.model.Wallet
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import com.android.l2l.twolocal.model.event.RefreshWalletListEvent
import com.android.l2l.twolocal.ui.wallet.di.DaggerWalletComponent
import com.android.l2l.twolocal.utils.MessageUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

@ExperimentalCoroutinesApi
class BottomSheetRemoveWallet : BottomSheetDialogFragment() { //TODO create base bottom sheet

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    val viewModel: RemoveWalletViewModel by viewModels { viewModelFactory }
    private val binding: BottomsheetRemoveWalletBinding by viewBinding()
    private var walletType: CryptoCurrencyType? = null
    private var wallet: Wallet? = null

    companion object {
        const val WALLET_KEY = "WALLET_KEY"

        fun newInstance(walletType: CryptoCurrencyType) = BottomSheetRemoveWallet().apply {
            arguments = bundleOf(
                WALLET_KEY to walletType
            )
        }
    }

    private fun handelBundle(arguments: Bundle?) {
        arguments?.let {
            walletType = it.get(WALLET_KEY) as CryptoCurrencyType
        }
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        handelBundle(arguments)
        DaggerWalletComponent.factory().create(requireActivity().findAppComponent(), walletType).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handelBundle(arguments)

        viewModel.removeWalletLiveData.observe(viewLifecycleOwner, {res->
            when (res) {
                is ViewState.Success -> {
                    EventBus.getDefault().post(RefreshWalletListEvent())
//                    MessageUtils.showSuccessDialog(requireContext(), getString(R.string.bottom_sheet_remove_wallet)).show()

                    MessageUtils.showSuccessDialog(getString(R.string.bottom_sheet_remove_wallet), requireContext()) {
                        this.dismiss()
                        callbackClickListener?.onCallbackClick(res.response)
                    }.show()
                }
                else -> {
                }
            }
        })

        viewModel.walletLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Success -> {
                    wallet = it.response
                    wallet?.let { wallet->
                        binding.txtWalletType.text = wallet.walletName
                    }
                }
                else -> {
                }
            }
        })

        walletType?.let { viewModel.getWalletDetail(it) }

        binding.btnConfirm.setOnClickListener {
            removeWalletConfirm()
        }

        binding.imgClose.setOnClickListener {
            this.dismiss()
        }

        binding.btnCancel.setOnClickListener {
            this.dismiss()
        }
    }

    private fun removeWalletConfirm(){
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.bottom_sheet_remove_wallet_question))
            .setMessage(getString(R.string.bottom_sheet_remove_wallet_please_write_recovery))
            .setPositiveButton(
                R.string.bottom_sheet_remove_btn_submit
            ) { _, _ ->  walletType?.let { it -> viewModel.removeWallet(it) } }
            .setNegativeButton(R.string.cancel, null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    private var callbackClickListener: OnCallbackListener? = null

    interface OnCallbackListener {
        fun onCallbackClick(success: Boolean)
    }

    fun setOnRenameClickListener(mRemoveClickListener: OnCallbackListener) {
        this.callbackClickListener = mRemoveClickListener
    }
}