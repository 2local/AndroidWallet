package com.android.l2l.twolocal.ui.wallet.di

import androidx.lifecycle.ViewModel
import com.android.l2l.twolocal.di.ViewModelKey
import com.android.l2l.twolocal.ui.wallet.detail.WalletDetailViewModel
import com.android.l2l.twolocal.ui.wallet.detail.remove.RemoveWalletViewModel
import com.android.l2l.twolocal.ui.wallet.detail.rename.RenameWalletViewModel
import com.android.l2l.twolocal.ui.wallet.detail.transactionDetail.TransactionDetailViewModel
import com.android.l2l.twolocal.ui.wallet.create.viewmoel.*
import com.android.l2l.twolocal.ui.wallet.home.HomeViewModel
import com.android.l2l.twolocal.ui.wallet.myWalletList.MyWalletListViewModel
import com.android.l2l.twolocal.ui.wallet.receive.ReceiveViewModel
import com.android.l2l.twolocal.ui.wallet.send.viewmodel.SendViewModel
import com.android.l2l.twolocal.ui.wallet.send.confirm.SendConfirmViewModel
import com.android.l2l.twolocal.ui.wallet.transaction.TransactionHistoryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi


@Suppress("unused")
@Module
abstract class ViewModelWalletModule {
    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(MyWalletListViewModel::class)
    abstract fun bindMyWalletListViewModel(myWalletListViewModel: MyWalletListViewModel): ViewModel

    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(CreateWalletViewModel::class)
    abstract fun bindCreateWalletViewModel(createWalletViewModel: CreateWalletViewModel): ViewModel

    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(EtherBackupViewModel::class)
    abstract fun bindEtherBackupViewModel(etherBackupViewModel: EtherBackupViewModel): ViewModel

    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(EtherRestoreViewModel::class)
    abstract fun bindEtherRestoreViewModel(etherRestoreViewModel: EtherRestoreViewModel): ViewModel

    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(EtherSuccessViewModel::class)
    abstract fun bindEtherSuccessViewModel(etherSuccessViewModel: EtherSuccessViewModel): ViewModel

    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(EtherVerifyMnemonicViewModel::class)
    abstract fun bindEtherVerifyMnemonicViewModel(etherVerifyMnemonicViewModel: EtherVerifyMnemonicViewModel): ViewModel

    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(WalletDetailViewModel::class)
    abstract fun bindWalletDetailViewModel(walletDetailViewModel: WalletDetailViewModel): ViewModel

    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(RenameWalletViewModel::class)
    abstract fun bindRenameWalletViewModel(renameWalletViewModel: RenameWalletViewModel): ViewModel

    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(RemoveWalletViewModel::class)
    abstract fun bindRemoveWalletViewModel(removeWalletViewModel: RemoveWalletViewModel): ViewModel

    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(TransactionDetailViewModel::class)
    abstract fun bindTransactionDetailViewModel(transactionDetailViewModel: TransactionDetailViewModel): ViewModel

    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(ReceiveViewModel::class)
    abstract fun bindReceiveViewModel(receiveViewModel: ReceiveViewModel): ViewModel

    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(SendViewModel::class)
    abstract fun bindSendViewModel(sendViewModel: SendViewModel): ViewModel

    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(TransactionHistoryViewModel::class)
    abstract fun bindTransactionHistoryViewModel(transactionHistoryViewModel: TransactionHistoryViewModel): ViewModel

    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(SendConfirmViewModel::class)
    abstract fun bindSendConfirmViewModel(sendConfirmViewModel: SendConfirmViewModel): ViewModel
}