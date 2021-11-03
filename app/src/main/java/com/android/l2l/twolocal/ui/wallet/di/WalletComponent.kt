package com.android.l2l.twolocal.ui.wallet.di

import com.android.l2l.twolocal.di.component.AppComponent
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import com.android.l2l.twolocal.ui.wallet.create.*
import com.android.l2l.twolocal.ui.wallet.detail.WalletDetailActivity
import com.android.l2l.twolocal.ui.wallet.detail.remove.BottomSheetRemoveWallet
import com.android.l2l.twolocal.ui.wallet.detail.rename.BottomSheetRenameWallet
import com.android.l2l.twolocal.ui.wallet.detail.transactionDetail.BottomSheetTransactionDetail
import com.android.l2l.twolocal.ui.wallet.home.FragmentHomeTab
import com.android.l2l.twolocal.ui.wallet.myWalletList.FragmentWalletListTab
import com.android.l2l.twolocal.ui.wallet.receive.ReceiveActivity
import com.android.l2l.twolocal.ui.wallet.send.SendTokenFragment
import com.android.l2l.twolocal.ui.wallet.send.SendTokenActivity
import com.android.l2l.twolocal.ui.wallet.send.SendTokenSuccessFragment
import com.android.l2l.twolocal.ui.wallet.send.confirm.SendTokenConfirmFragment
import com.android.l2l.twolocal.ui.wallet.transaction.TransactionHistoryActivity
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@WalletScope
@Component(dependencies = [AppComponent::class], modules = [ViewModelWalletModule::class, WalletModule::class])
interface WalletComponent {
    fun inject(fragmentHome: FragmentHomeTab)
    fun inject(fragmentMyWalletList: FragmentWalletListTab)
    fun inject(etherVerifyMnemonic: WalletVerifyMnemonic)
    fun inject(walletRestoreMnemonicFragment: WalletRestoreMnemonicFragment)
    fun inject(walletRestorePrivateKeyFragment: WalletRestorePrivateKeyFragment)
    fun inject(createEtherWalletActivity: CreateWalletActivity)
    fun inject(createWalletListFragment: CreateWalletListFragment)
    fun inject(etherBackupSuccessFragment: WalletBackupSuccessFragment)
    fun inject(etherBackupFragment: WalletBackupFragment)
    fun inject(walletDetailActivity: WalletDetailActivity)
    fun inject(bottomSheetRemoveWallet: BottomSheetRemoveWallet)
    fun inject(renameWallet: BottomSheetRenameWallet)
    fun inject(bottomSheetTransactionDetail: BottomSheetTransactionDetail)
    fun inject(receiveActivity: ReceiveActivity)
    fun inject(sendFragment: SendTokenFragment)
    fun inject(sendActivity: SendTokenActivity)
    fun inject(sendTokenConfirmationFragment: SendTokenSuccessFragment)
    fun inject(transactionDetailActivity: TransactionHistoryActivity)
    fun inject(sendTokenConfirmFragment: SendTokenConfirmFragment)

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent, @BindsInstance walletType: CryptoCurrencyType? = null): WalletComponent
    }
}