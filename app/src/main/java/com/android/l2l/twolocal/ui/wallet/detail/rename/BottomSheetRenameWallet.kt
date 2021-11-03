package com.android.l2l.twolocal.ui.wallet.detail.rename

import android.os.Bundle
import android.view.KeyEvent.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.common.findAppComponent
import com.android.l2l.twolocal.common.hideKeyboard
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.databinding.BottomsheetRenameWalletBinding
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.model.Wallet
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import com.android.l2l.twolocal.model.event.RefreshWalletListEvent
import com.android.l2l.twolocal.ui.wallet.detail.remove.BottomSheetRemoveWallet
import com.android.l2l.twolocal.ui.wallet.di.DaggerWalletComponent
import com.android.l2l.twolocal.utils.MessageUtils
import com.android.l2l.twolocal.utils.setEditTextError
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

@ExperimentalCoroutinesApi
class BottomSheetRenameWallet : BottomSheetDialogFragment() { //TODO create base bottom sheet

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    val viewModel: RenameWalletViewModel by viewModels { viewModelFactory }
    private val binding: BottomsheetRenameWalletBinding by viewBinding()
    private var walletType: CryptoCurrencyType? = null
    private var wallet: Wallet? = null

    companion object {
        const val WALLET_KEY = "WALLET_KEY"

        fun newInstance(walletType: CryptoCurrencyType) = BottomSheetRenameWallet().apply {
            arguments = bundleOf(
                WALLET_KEY to walletType
            )
        }
    }

    private fun handelBundle(arguments: Bundle?) {
        arguments?.let {
            walletType = it.get(BottomSheetRemoveWallet.WALLET_KEY) as CryptoCurrencyType
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

        binding.edtWalletName.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KEYCODE_ENTER) {
                hideKeyboard()
                return@setOnKeyListener true
            }
            false
        }

        viewModel.validForm.observe(viewLifecycleOwner, {
            when (it) {
                is RenameFormState.NameError -> {
                    binding.edtWalletName.setEditTextError(it.error)
                }

                else -> {
                }
            }
        })


        viewModel.updateWalletLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Success -> {
                    EventBus.getDefault().post(RefreshWalletListEvent())
                    MessageUtils.showSuccessDialog(requireContext(), getString(R.string.bottom_sheet_rename_wallet_updated)).show()
                    mRemoveClickListener?.onWalletRenameClick(it.response.walletName)
                    this.dismiss()
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
                        binding.edtWalletName.setText(wallet.walletName)
                    }
                }
                else -> {
                }
            }
        })
        walletType?.let { viewModel.getWalletDetail(it) }

        binding.btnConfirm.setOnClickListener {
            wallet?.let { wal->
                val walletName = binding.edtWalletName.text.toString().trim()
                wal.walletName = walletName
                viewModel.saveWallet(wal)
             }
        }

        binding.imgClose.setOnClickListener {
            this.dismiss()
        }
        binding.btnCancel.setOnClickListener {
            this.dismiss()
        }
    }

    private var mRemoveClickListener: OnRenameClickListener? = null

    interface OnRenameClickListener {
        fun onWalletRenameClick(name: String)
    }

    fun setOnRenameClickListener(mRemoveClickListener: OnRenameClickListener) {
        this.mRemoveClickListener = mRemoveClickListener
    }
}