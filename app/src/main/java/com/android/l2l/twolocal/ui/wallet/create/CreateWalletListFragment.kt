package com.android.l2l.twolocal.ui.wallet.create

import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.common.findAppComponent
import com.android.l2l.twolocal.common.onErrorDialog
import com.android.l2l.twolocal.common.visible
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.databinding.FragmentCreateWalletListBinding
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.model.Wallet
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import com.android.l2l.twolocal.ui.base.BaseFragment
import com.android.l2l.twolocal.ui.wallet.di.DaggerWalletComponent
import com.android.l2l.twolocal.ui.wallet.create.viewmoel.CreateWalletViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class CreateWalletListFragment : BaseFragment<CreateWalletViewModel>(R.layout.fragment_create_wallet_list) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    override val viewModel: CreateWalletViewModel by viewModels { viewModelFactory }
    private val binding: FragmentCreateWalletListBinding by viewBinding(FragmentCreateWalletListBinding::bind)

    private lateinit var adapter: CreateWalletAdapter
    private val wallets: MutableList<Wallet> = arrayListOf()
    var walletType: CryptoCurrencyType? = null

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        DaggerWalletComponent.factory().create(requireActivity().findAppComponent()).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewWallets.layoutManager = LinearLayoutManager(requireContext())
        adapter = CreateWalletAdapter(requireContext(), wallets)
        binding.recyclerViewWallets.adapter = adapter

        adapter.setItemClickListener { view, position ->
            walletType = wallets[position].type
            for (w in wallets) {
                if (w.isSelected()) {
                    if (walletType!= CryptoCurrencyType.TwoLC && walletType!= CryptoCurrencyType.BINANCE)
                    binding.btnCreateWallet.visible()
                    binding.btnRestoreWallet.visible()
                }
            }
        }

        viewModel.walletListLiveData.observe(requireActivity(), {
            when (it) {
                is ViewState.Success -> {
                    hideLoading()
                    wallets.clear()
                    wallets.addAll(it.response)
                    adapter.notifyDataSetChanged()
                }
                is ViewState.Error -> {
                    hideLoading()
                    onErrorDialog(it.error.message)
                }
                else -> {
                }
            }
        })

        viewModel.getAvailableWalletList()

        binding.btnCreateWallet.setOnClickListener {
            if (walletType != null) {
                val bundle = bundleOf(
                    WalletBackupFragment.WALLET_TYPE_KEY to walletType,
                )
                findNavController().navigate(R.id.action_create_wallet_list_dest_to_etherBackupFragment, bundle)
            }
        }

        binding.toolbar.getCloseBtn().setOnClickListener {
            requireActivity().finish()
        }

        binding.btnRestoreWallet.setOnClickListener {
            if (walletType != null) {
                if (walletType == CryptoCurrencyType.TwoLC || walletType== CryptoCurrencyType.BINANCE) {
                    val bundle = bundleOf(
                        WalletRestorePrivateKeyFragment.WALLET_TYPE_KEY to walletType,
                    )
                    findNavController().navigate(R.id.action_create_wallet_list_dest_to_walletRestorePrivateKeyFragment, bundle)
                } else {
                    val bundle = bundleOf(
                        WalletRestoreMnemonicFragment.WALLET_TYPE_KEY to walletType,
                    )
                    findNavController().navigate(R.id.action_create_wallet_list_dest_to_etherRestoreFragment, bundle)
                }
            }
        }
    }
}