package com.android.l2l.twolocal.dataSourse.repository.contact

import com.android.l2l.twolocal.model.AddressBook
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import io.reactivex.Single

interface ContactRepositoryHelper {

    fun getContactList(): Single<List<AddressBook>>
    fun getContactList(walletType: CryptoCurrencyType): Single<List<AddressBook>>
    fun addContact(addressBook: AddressBook): Single<Long>
    fun deleteSingleContact(addressBook: AddressBook): Single<Boolean>


}