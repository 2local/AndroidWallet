package com.android.l2l.twolocal.ui.wallet.create

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.common.findAppComponent
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.databinding.FragmentWalletVerifyMnemonicBinding
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import com.android.l2l.twolocal.ui.base.BaseFragment
import com.android.l2l.twolocal.ui.wallet.di.DaggerWalletComponent
import com.android.l2l.twolocal.ui.wallet.create.viewmoel.EtherVerifyMnemonicViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
class WalletVerifyMnemonic : BaseFragment<EtherVerifyMnemonicViewModel>(R.layout.fragment_wallet_verify_mnemonic) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    override val viewModel: EtherVerifyMnemonicViewModel by viewModels { viewModelFactory }
    private val binding: FragmentWalletVerifyMnemonicBinding by viewBinding(FragmentWalletVerifyMnemonicBinding::bind)

    var wordList = listOf<String>()
    var lastFourWords = arrayListOf<String>()
    var firstEightWord = ""
    lateinit var walletType: CryptoCurrencyType

    companion object {
        const val WALLET_TYPE_KEY = "WALLET_TYPE_KEY"
        const val NMEMONIC_SPACE_SEPARATOR = "   ";
    }

    private fun handleBundle(arguments: Bundle?) {
        walletType = arguments?.get(WALLET_TYPE_KEY) as CryptoCurrencyType
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        handleBundle(arguments)
        DaggerWalletComponent.factory().create(requireActivity().findAppComponent(), walletType).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtCreateWallet.text = getString(R.string.create_wallet, walletType.symbol)

        setUpListeners()

        binding.btnGoNext.setOnClickListener {
            val mnemonic = binding.tvMnemonic.text.toString().replace(NMEMONIC_SPACE_SEPARATOR, " ")
            viewModel.verifyMnemonic(walletType, mnemonic.trim())
        }

        binding.toolbar.getCloseBtn().setOnClickListener {
            findNavController().popBackStack()
        }

        binding.tvClear.setOnClickListener {
            firstEightWord = getFirstEightList()
            lastFourWords = getLastFourList()
            binding.tvMnemonic.text = firstEightWord
            addLstFourWordsToView()
        }

        viewModel.loadMnemonic(walletType)
    }

    private fun setUpListeners() {
        viewModel.walletLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Loading -> {
                    showLoading()
                }
                is ViewState.Success -> {
                    hideLoading()
                    onMnemonicLoaded(it.response)
                }
                is ViewState.Error -> {
                    hideLoading()
                    onErrorDialog(it.error.message)
                }
            }
        })

        viewModel.verifyMnemonicLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Loading -> {
                    showLoading()
                }
                is ViewState.Success -> {
                    hideLoading()
                    onVerifiedSuccessfully()
                }
                is ViewState.Error -> {
                    hideLoading()
                    onErrorDialog(it.error.message)
                }
            }
        })

    }

    private fun onVerifiedSuccessfully() {
        val bundle = bundleOf(
            WalletBackupSuccessFragment.MESSAGE_KEY to getString(R.string.fragment_wallet_backup_success_backup_eth_success, walletType.symbol),
            WalletBackupSuccessFragment.WALLET_TYPE_KEY to walletType,
        )
        findNavController().navigate(R.id.action_etherVerifyMnemonic_to_etherBackupSuccessFragment, bundle)
    }


    fun onMnemonicLoaded(mnemonic: String?) {
        mnemonic?.let {
            wordList = it.split(" ")
        }
        firstEightWord = getFirstEightList()
        lastFourWords = getLastFourList()
        binding.tvMnemonic.text = firstEightWord

        addLstFourWordsToView()
    }

    private fun getFirstEightList(): String {
        var firstEightWord = ""
        wordList.forEachIndexed { index, element ->
            if (index < 8)
                firstEightWord += "$element$NMEMONIC_SPACE_SEPARATOR"
        }
        return firstEightWord
    }

    private fun getLastFourList(): ArrayList<String> {
        val lastFourWords = arrayListOf<String>()
        wordList.forEachIndexed { index, element ->
            if (index >= 8)
                lastFourWords.add(element)
        }
        lastFourWords.shuffle()
        return lastFourWords
    }

    private fun addLstFourWordsToView() {
        binding.flowWorldList.removeAllViews()
        lastFourWords.forEach { word ->
            val textView = TextView(context)
            textView.text = word
            textView.setBackgroundResource(R.drawable.bg_white_border_gray_radius_normal)
            val padding = resources.getDimension(R.dimen.margin_intermediate).toInt()
            textView.setPadding(padding, padding, padding, padding)

            binding.flowWorldList.addView(textView)
            val params = textView.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(0, 0, padding, padding)
            textView.layoutParams = params

            textView.setOnClickListener {
                lastFourWords.remove(word)
                firstEightWord += "$word   "
                binding.tvMnemonic.text = firstEightWord
                addLstFourWordsToView()
            }
        }
    }


}