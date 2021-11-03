package com.android.l2l.twolocal.ui.wallet.detail

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.common.findAppComponent
import com.android.l2l.twolocal.common.gone
import com.android.l2l.twolocal.common.visible
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.databinding.ActivityWalletDetailBinding
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.model.Wallet
import com.android.l2l.twolocal.model.WalletTransactionHistory
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import com.android.l2l.twolocal.model.event.RefreshWalletEvent
import com.android.l2l.twolocal.model.event.RefreshWalletListEvent
import com.android.l2l.twolocal.ui.base.BaseActivity
import com.android.l2l.twolocal.ui.wallet.detail.remove.BottomSheetRemoveWallet
import com.android.l2l.twolocal.ui.wallet.detail.rename.BottomSheetRenameWallet
import com.android.l2l.twolocal.ui.wallet.detail.transactionDetail.BottomSheetTransactionDetail
import com.android.l2l.twolocal.ui.wallet.di.DaggerWalletComponent
import com.android.l2l.twolocal.ui.wallet.receive.ReceiveActivity
import com.android.l2l.twolocal.ui.wallet.send.SendTokenActivity
import com.android.l2l.twolocal.utils.CommonUtils
import com.android.l2l.twolocal.utils.SecurityUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

@ExperimentalCoroutinesApi
class WalletDetailActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    private val viewModel: WalletDetailViewModel by viewModels { viewModelFactory }
    private val binding: ActivityWalletDetailBinding by viewBinding(ActivityWalletDetailBinding::inflate)

    private var walletType: CryptoCurrencyType? = null
    private var wallet: Wallet? = null

    companion object {
        const val WALLET_KEY = "WALLET_KEY"

        fun start(context: Context, walletType: CryptoCurrencyType) {
            Intent(context, WalletDetailActivity::class.java).apply {
                val bundle = bundleOf(
                    WALLET_KEY to walletType,
                )
                putExtras(bundle)
                context.startActivity(this)
            }
        }
    }

    private fun handleBundle(bundle: Bundle?) {
        walletType = bundle?.get(WALLET_KEY) as CryptoCurrencyType
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handleBundle(intent.extras)
        DaggerWalletComponent.factory().create(findAppComponent(), walletType).inject(this)

        binding.recyclerHistory.layoutManager = LinearLayoutManager(this)


        binding.btnSend.setOnClickListener {
            wallet?.let {
                    SendTokenActivity.start(this, it.type)
            }
        }

        binding.btnReceive.setOnClickListener {
            wallet?.let {
                    ReceiveActivity.start(this, it.type)
            }
        }

        binding.btnBuy.setOnClickListener {
        }

        binding.toolbar.getBackBtn().setOnClickListener {
            finish()
        }

        binding.toolbar.getMenuBtn().setOnClickListener {
            showPopUpMenu(it)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            getBalance()
            getTransactionHistory()
        }


//        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                if (tab.position == 0) {
//                    viewModel.getAllTransferDetails()
//                } else if (tab.position == 1) {
//                    viewModel.getTransferOrderDetails()
//                } else if (tab.position == 2) {
//                    viewModel.getPurchaseTransactions()
//                }
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab) {
//
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab) {
//
//            }
//        })

        setUpListeners()
        getWalletDetail()
        getBalance()
    }

    private fun setUpListeners() {
        viewModel.transactionHistoryLiveData.observe(this, {
            when (it) {
                is ViewState.Loading -> {
                    binding.progress.visible()
                }
                is ViewState.Success -> {
                    binding.progress.gone()
                    if (it.response.isNullOrEmpty()) {
                        binding.lntNoData.visible()
                    } else {
                        binding.lntNoData.gone()
                        setUpTransactionHistoryListAdapter(it.response)
                    }

                }
                is ViewState.Error -> {
                    binding.progress.gone()
                    onMessageToast(it.error.message)
                }
            }
        })

        viewModel.walletLiveData.observe(this, {
            when (it) {
                is ViewState.Loading -> {
                    showLoading()
                }
                is ViewState.Success -> {
                    hideLoading()
//                    binding.swipeRefreshLayout.isRefreshing = false
                    wallet = it.response
                    setWalletInfo(wallet)
                    getTransactionHistory()
                }
                is ViewState.Error -> {
                    hideLoading()
//                    binding.swipeRefreshLayout.isRefreshing = false
                    onMessageToast(it.error.message)
                }
            }
        })

        viewModel.balanceLiveData.observe(this, {
            when (it) {
                is ViewState.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }
                is ViewState.Success -> {
                    hideLoading()
                    binding.swipeRefreshLayout.isRefreshing = false
                    wallet = it.response
                    setWalletBalance(wallet)
                    EventBus.getDefault().post(RefreshWalletListEvent())
                }
                is ViewState.Error -> {
                    hideLoading()
                    binding.swipeRefreshLayout.isRefreshing = false
                    onMessageToast(it.error.message)
                }
            }
        })
    }

    override fun onResume() {
        getTransactionHistory()
        super.onResume()
    }

    private fun setUpTransactionHistoryListAdapter(transactionList: List<WalletTransactionHistory>) {
        val adapter = TransactionsRecyclerAdapter(this, transactionList)
        adapter.setItemClickListener { view, position ->
            val bottomSheetTransactionDetail: BottomSheetTransactionDetail =
                BottomSheetTransactionDetail.newInstance(walletType!!, transactionList[position].hash)
            bottomSheetTransactionDetail.show(this.supportFragmentManager, bottomSheetTransactionDetail.tag)
        }
        binding.recyclerHistory.adapter = adapter
    }

    private fun getBalance() {
        walletType?.let {
            viewModel.getWalletBalance(it)
        }
    }

    private fun getWalletDetail() {
        walletType?.let {
            viewModel.getWalletDetail(it)
        }
    }

    private fun setWalletBalance(wallet: Wallet?) {
        wallet?.let {
            binding.txtTokenAmount.text = it.amountPriceFormat
            binding.txtUSDAmount.text = String.format("%s%s", it.currency.mySymbol, it.fiatPriceFormat)
        }
    }

    private fun setWalletInfo(wallet: Wallet?) {
        wallet?.let {
            setWalletBalance(it)
            binding.imgCoin.setImageResource(it.type.icon)
            binding.txtTokenSymbol.text = it.type.symbol
            binding.txtWalletName.text = it.walletName

        }
    }

    private fun getTransactionHistory() {
//        wallet?.let {
            viewModel.getTransactionHistory()
//        }
    }

    @SuppressLint("RestrictedApi")
    private fun showPopUpMenu(v: View) {
        val popup = PopupMenu(this, v)
        popup.menuInflater.inflate(R.menu.wallet_menu, popup.menu)
        if (popup.menu is MenuBuilder) {
            val menuBuilder = popup.menu as MenuBuilder


            menuBuilder.setOptionalIconsVisible(true)
            for (item in menuBuilder.visibleItems) {
                val iconMarginPx = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    resources.getDimension(R.dimen.margin_xsmall),
                    resources.displayMetrics
                ).toInt()
                if (item.icon != null) {
                    item.icon = InsetDrawable(item.icon, iconMarginPx, 0, 0, 0)
                }
            }
        }

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.wallet_menu_remove -> {
                    showRemoveWalletBottomSheet()
                }
                R.id.wallet_menu_rename -> {
                    showRenameWalletBottomSheet()
                }
                R.id.wallet_menu_mnemonic -> {
                    showMnemonic()
                }
            }
            true
        }

        popup.show()
    }

    private fun showRemoveWalletBottomSheet() {
        wallet?.let {
            val bottomSheetRemoveWallet: BottomSheetRemoveWallet = BottomSheetRemoveWallet.newInstance(it.type)
            bottomSheetRemoveWallet.setOnRenameClickListener(object : BottomSheetRemoveWallet.OnCallbackListener {
                override fun onCallbackClick(success: Boolean) {
                    finish()
                }
            })
            bottomSheetRemoveWallet.show(this.supportFragmentManager, bottomSheetRemoveWallet.tag)
        }
    }

    private fun showRenameWalletBottomSheet() {
        wallet?.let {
            val bottomSheetRenameWallet: BottomSheetRenameWallet = BottomSheetRenameWallet.newInstance(it.type)
            bottomSheetRenameWallet.setOnRenameClickListener(object : BottomSheetRenameWallet.OnRenameClickListener {
                override fun onWalletRenameClick(name: String) {
                    binding.txtWalletName.text = name
                }
            })
            bottomSheetRenameWallet.show(this.supportFragmentManager, bottomSheetRenameWallet.tag)
        }
    }


    private fun showMnemonic() {
        wallet?.let {
            var mnemonic = ""
            if (!it.mnemonic.isNullOrBlank())
                mnemonic = SecurityUtils.getSecureString(it.mnemonic, it.address, this)
            else if (!it.privateKey.isNullOrBlank())
                mnemonic = SecurityUtils.getSecureString(it.privateKey, it.address, this)

            showMessage(mnemonic, getString(R.string.wallet_mnemonic))
        }
    }

    private fun showMessage(message: String, title: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(
                R.string.activity_wallet_detail_copy
            ) { dialog, which -> CommonUtils.copyToClipboard(this, message) }
            .setNegativeButton(R.string.cancel, null)
            .setIcon(android.R.drawable.ic_dialog_info)
            .show()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: RefreshWalletEvent) {
        getBalance()
    }
}