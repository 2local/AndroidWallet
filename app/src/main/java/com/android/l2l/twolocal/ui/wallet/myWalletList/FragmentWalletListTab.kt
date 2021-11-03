package com.android.l2l.twolocal.ui.wallet.myWalletList

import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.common.findAppComponent
import com.android.l2l.twolocal.common.gone
import com.android.l2l.twolocal.common.invisible
import com.android.l2l.twolocal.common.visible
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.databinding.FragmentWalletListTabBinding
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.model.Wallet
import com.android.l2l.twolocal.model.event.RefreshWalletListEvent
import com.android.l2l.twolocal.ui.base.BaseFragment
import com.android.l2l.twolocal.ui.wallet.detail.WalletDetailActivity
import com.android.l2l.twolocal.ui.wallet.di.DaggerWalletComponent
import com.android.l2l.twolocal.ui.wallet.create.CreateWalletActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

@ExperimentalCoroutinesApi
class FragmentWalletListTab : BaseFragment<MyWalletListViewModel>(R.layout.fragment_wallet_list_tab) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    override val viewModel: MyWalletListViewModel by viewModels { viewModelFactory }
    private val binding: FragmentWalletListTabBinding by viewBinding(FragmentWalletListTabBinding::bind)

    private lateinit var adapter: MyWalletRecyclerViewAdapter
    private val wallets: MutableList<Wallet> = arrayListOf()

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        DaggerWalletComponent.factory().create(requireActivity().findAppComponent()).inject(this)
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().register(this)

        binding.recyclerViewWallets.layoutManager = LinearLayoutManager(requireContext())
        adapter = MyWalletRecyclerViewAdapter(requireContext(), wallets) { view, position ->
            WalletDetailActivity.start(requireContext(), wallets[position].type)
        }

        binding.recyclerViewWallets.adapter = adapter


        viewModel.walletListLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Loading -> {
                    showLoading()
                }
                is ViewState.Success -> {
                    hideLoading()
                    setUpWalletList(it.response)
                }
                is ViewState.Error -> {
                    hideLoading()
                    onMessageToast(it.error.message)
                }
            }
        })

        viewModel.canCreateWalletLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Success -> {
                    if(it.response)
                        binding.btnAddWallet.visible()
                    else
                        binding.btnAddWallet.invisible()
                }
                else -> {
                }
            }
        })
        viewModel.getMyWalletList()

        binding.btnAddWallet.setOnClickListener {
            CreateWalletActivity.start(requireContext())
        }
    }

    private fun setUpWalletList(walletList: List<Wallet>) {
        wallets.clear()
        wallets.addAll(walletList)
        if(wallets.isEmpty())
            binding.lntNoData.visible()
        else
            binding.lntNoData.gone()
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: RefreshWalletListEvent) {
        viewModel.getMyWalletList()
    }

}