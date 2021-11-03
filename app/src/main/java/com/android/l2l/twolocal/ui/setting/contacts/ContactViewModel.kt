package com.android.l2l.twolocal.ui.setting.contacts;

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.l2l.twolocal.common.withIO
import com.android.l2l.twolocal.dataSourse.repository.contact.ContactRepositoryHelper
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.dataSourse.utils.error.GeneralError
import com.android.l2l.twolocal.dataSourse.utils.error.withError
import com.android.l2l.twolocal.model.AddressBook
import com.android.l2l.twolocal.ui.base.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class ContactViewModel
@Inject constructor(
    private val contactRepository: ContactRepositoryHelper,
) : BaseViewModel() {

    private val _contactListLiveData = MutableLiveData<ViewState<List<AddressBook>>>()
    val contactListLiveData: LiveData<ViewState<List<AddressBook>>>
        get() = _contactListLiveData

    private val _deleteContactListLiveData = MutableLiveData<ViewState<Boolean>>()
    val deleteContactLiveData: LiveData<ViewState<Boolean>>
        get() = _deleteContactListLiveData

    fun getContactList() {
        contactRepository.getContactList().withIO()
            .doOnSubscribe {
                addToDisposable(it)
                _contactListLiveData.value = ViewState.Loading
            }
            .doOnError { _contactListLiveData.value = ViewState.Error(GeneralError().withError(it)) }
            .subscribe({
                _contactListLiveData.value = ViewState.Success(it)
            }, { })
    }


    fun deleteSingleContact(addressBook: AddressBook) {
        contactRepository.deleteSingleContact(addressBook).withIO()
            .doOnSubscribe {
                addToDisposable(it)
                _deleteContactListLiveData.value = ViewState.Loading
            }
            .doOnError { _deleteContactListLiveData.value = ViewState.Error(GeneralError().withError(it)) }
            .subscribe({
                _deleteContactListLiveData.value = ViewState.Success(it)
                getContactList()
            }, { })
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}