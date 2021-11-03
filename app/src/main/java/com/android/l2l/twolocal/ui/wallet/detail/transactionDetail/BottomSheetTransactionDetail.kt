package com.android.l2l.twolocal.ui.wallet.detail.transactionDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.common.findAppComponent
import com.android.l2l.twolocal.common.gone
import com.android.l2l.twolocal.common.visible
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.databinding.BottomsheetTransactionDetailBinding
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import com.android.l2l.twolocal.ui.wallet.di.DaggerWalletComponent
import com.android.l2l.twolocal.utils.CommonUtils
import com.android.l2l.twolocal.utils.WalletFactory
import com.android.l2l.twolocal.utils.MessageUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class BottomSheetTransactionDetail : BottomSheetDialogFragment() { //TODO create base bottom sheet

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    val viewModel: TransactionDetailViewModel by viewModels { viewModelFactory }
    private val binding: BottomsheetTransactionDetailBinding by viewBinding()
    private var transactionHashCode: String? = null
    private var walletType: CryptoCurrencyType? = null

    companion object {
        const val TRANSACTION_CODE_KEY = "TRANSACTION_CODE_KEY"
        const val WALLET_TYPE_KEY = "WALLET_TYPE_KEY"

        fun newInstance(walletType: CryptoCurrencyType, transactionHashCode: String) = BottomSheetTransactionDetail().apply {
            arguments = bundleOf(
                TRANSACTION_CODE_KEY to transactionHashCode,
                WALLET_TYPE_KEY to walletType
            )
        }
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        walletType = arguments?.get(WALLET_TYPE_KEY) as CryptoCurrencyType
        DaggerWalletComponent.factory().create(requireActivity().findAppComponent(), walletType).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            transactionHashCode = it.getString(TRANSACTION_CODE_KEY)
        }


        viewModel.transactionLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Error -> {
                    binding.progressBar.gone()
                    MessageUtils.showErrorDialog(requireContext(), it.error?.message)
                }
                is ViewState.Loading -> {
                    binding.progressBar.visible()
                }
                is ViewState.Success -> {
                    binding.progressBar.gone()
                    binding.txtHashCode.text = getString(R.string.transaction_detail_hash, "\n${it.response.hash}") //
                    binding.txtDetail.text =
                        getString(R.string.transaction_detail_address_from_to, "\n${it.response.from}\n\n", "\n${it.response.to}\n\n")
                    binding.txtStatus.text = getString(R.string.transaction_detail_status, getString(it.response.txreceiptStatusString))
                    if (it.response.txreceiptStatus == "0x1" || it.response.txreceiptStatus == "1")
                        binding.txtStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                    else if (it.response.txreceiptStatus == "2")//pending
                        binding.txtStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                    else
                        binding.txtStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.button_brown))
                }
                else -> {

                }
            }
        })

        transactionHashCode?.let { viewModel.getTransactionDetail(it) }

        binding.imgClose.setOnClickListener {
            this.dismiss()
        }

        binding.btnCopy.setOnClickListener {
            CommonUtils.copyToClipboard(requireContext(), WalletFactory.getTransactionScanUrl(walletType, transactionHashCode))
        }


    }

    private var callbackClickListener: OnCallbackListener? = null

    interface OnCallbackListener {
        fun onCallbackClick(success: Boolean)
    }

    fun setOnRenameClickListener(mRemoveClickListener: OnCallbackListener) {
        this.callbackClickListener = mRemoveClickListener
    }
}