package com.android.l2l.twolocal.ui.wallet.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.Nullable
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.common.findAppComponent
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.databinding.FragmentHomeTabBinding
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.model.Wallet
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import com.android.l2l.twolocal.model.enums.FiatType
import com.android.l2l.twolocal.model.event.RefreshWalletListEvent
import com.android.l2l.twolocal.ui.base.BaseFragment
import com.android.l2l.twolocal.ui.setting.SettingActivity
import com.android.l2l.twolocal.ui.wallet.di.DaggerWalletComponent
import com.android.l2l.twolocal.ui.wallet.create.CreateWalletActivity
import com.android.l2l.twolocal.ui.wallet.receive.ReceiveActivity
import com.android.l2l.twolocal.ui.wallet.send.SendTokenActivity
import com.android.l2l.twolocal.ui.wallet.transaction.TransactionHistoryActivity
import com.android.l2l.twolocal.utils.CommonUtils
import com.android.l2l.twolocal.utils.LiveDataCombineUtil
import com.android.l2l.twolocal.view.ChartItemView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import javax.inject.Inject


@ExperimentalCoroutinesApi
class FragmentHomeTab : BaseFragment<HomeViewModel>(R.layout.fragment_home_tab) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    override val viewModel: HomeViewModel by viewModels { viewModelFactory }
    private val binding: FragmentHomeTabBinding by viewBinding(FragmentHomeTabBinding::bind)
    private lateinit var adapter: WalletHomeRecyclerViewAdapter
    private val wallets: MutableList<Wallet> = arrayListOf()
    private lateinit var incomeViews: MutableList<ChartItemView>

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        DaggerWalletComponent.factory().create(requireActivity().findAppComponent(), CryptoCurrencyType.TwoLC).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().register(this)
        addViews()

        binding.recyclerViewWallets.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        adapter = WalletHomeRecyclerViewAdapter(requireContext(), wallets)
        binding.recyclerViewWallets.adapter = adapter
        adapter.setItemClickListener { view, position ->
            if (wallets[position].type == CryptoCurrencyType.NONE)
                CreateWalletActivity.start(requireContext())
            else if (view.id == R.id.btnReceive) {
                ReceiveActivity.start(requireContext(), wallets[position].type)
            } else if (view.id == R.id.btnSend) {
                SendTokenActivity.start(requireContext(), wallets[position].type)
            } else if (view.id == R.id.btnBuy) {

            }
        }

        viewModel.totalBalanceLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Success -> {
                    hideLoading()
                    binding.refreshLayout.isRefreshing = false
                    val wallet = it.response

                    if (wallet.showAmount)
                        binding.imageEye.setImageResource(R.drawable.ic_eye_gray)
                    else
                        binding.imageEye.setImageResource(R.drawable.ic_eye_brown_selected)
                    binding.txtTotalBalance.text = getString(
                        R.string.balance_currency,
                        wallet.currency,
                        if (wallet.showAmount) CommonUtils.formatToDecimalPriceTwoDigits(CommonUtils.stringToBigDecimal(wallet.balance)) else getString(
                            R.string.hidden_stars
                        )
                    )


                    binding.txtTotalBalanceDollar.text = getString(
                        R.string.balance_currency,
                        FiatType.USD.mySymbol,
                        if (wallet.showAmount) CommonUtils.formatToDecimalPriceSixDigits(CommonUtils.stringToBigDecimal(wallet.fiatBalance)) else getString(
                            R.string.hidden_stars
                        )
                    )

                }
                is ViewState.Error -> {
                    hideLoading()
                    binding.refreshLayout.isRefreshing = false
                    onMessageToast(it.error.message)
                }
                else -> {
                    binding.refreshLayout.isRefreshing = false
                }
            }
        })

        viewModel.walletListLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Success -> {
                    hideLoading()
                    wallets.clear()
                    wallets.addAll(it.response)
                    adapter.notifyDataSetChanged()
                }
                is ViewState.Error -> {
                    hideLoading()
                    onMessageToast(it.error.message)
                }
                else -> {
                }
            }
        })

        LiveDataCombineUtil.combine(viewModel.expenseLiveData, viewModel.incomeLiveData) { t1, t2 ->

            if (t1 is ViewState.Success && t2 is ViewState.Success) {
                val allValues: MutableList<Double> = (t1.response + t2.response).toMutableList()

                var maxValue = 0.0
                if (allValues.isNotEmpty())
                    maxValue = Collections.max(allValues)


                t2.response.forEachIndexed { index, element ->
                    incomeViews[index].setIncomeChart(maxValue, element)
                }

                t1.response.forEachIndexed { index, element ->
                    incomeViews[index].setExpenseFebChart(maxValue, element)
                }
            }

        }.observe(viewLifecycleOwner) { }

        binding.imageEye.setOnClickListener {
            viewModel.toggleBlindAmount()
        }

        binding.buttonTransactionDetails.setOnClickListener {
            TransactionHistoryActivity.start(requireContext())
        }

        binding.toolbar.getSettingBtn().setOnClickListener {
            SettingActivity.start(requireContext())
        }

        binding.refreshLayout.setOnRefreshListener {
            viewModel.getAllWallets()
        }


        lifecycleScope.launch {
            delay(300)
            viewModel.getAllWallets()
        }
        viewModel.getL2LTransactionsHistory()
    }

    private fun addViews() {

        incomeViews = arrayListOf()

        incomeViews.add(binding.chartJan)
        incomeViews.add(binding.chartFeb)
        incomeViews.add(binding.chartMar)
        incomeViews.add(binding.chartApr)
        incomeViews.add(binding.chartMay)
        incomeViews.add(binding.chartJun)
        incomeViews.add(binding.chartJul)
        incomeViews.add(binding.chartAug)
        incomeViews.add(binding.chartSep)
        incomeViews.add(binding.chartOct)
        incomeViews.add(binding.chartNov)
        incomeViews.add(binding.chartSep)

    }

    override fun onPause() {
        super.onPause()
        binding.refreshLayout.isEnabled = false
    }

    override fun onResume() {
        super.onResume()
        binding.refreshLayout.isEnabled = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: RefreshWalletListEvent) {
        viewModel.getAllWallets()
    }
}