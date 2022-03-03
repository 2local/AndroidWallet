package com.android.l2l.twolocal.ui.setting.contacts

import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.*
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.databinding.FragmentContactListBinding
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.model.AddressBook
import com.android.l2l.twolocal.ui.base.BaseFragment
import com.android.l2l.twolocal.ui.setting.di.DaggerSettingComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ContactListFragment : BaseFragment<ContactViewModel>(R.layout.fragment_contact_list) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    override val viewModel: ContactViewModel by viewModels { viewModelFactory }
    private val binding: FragmentContactListBinding by viewBinding(FragmentContactListBinding::bind)
    private lateinit var adapter: ContactsRecyclerAdapter
    private val contacts: MutableList<AddressBook> = arrayListOf()

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        DaggerSettingComponent.factory().create(requireActivity().findAppComponent()).inject(this)
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerContacts.layoutManager = LinearLayoutManager(requireContext())
        adapter = ContactsRecyclerAdapter(requireContext(), contacts) {
            viewModel.deleteSingleContact(it)
        }

        binding.recyclerContacts.adapter = adapter

        viewModel.contactListLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Success -> {
                    contacts.clear()
                    contacts.addAll(it.response)
                    if (contacts.isNotEmpty()) {
                        binding.addressbookContainerNoContact.gone()
                        binding.recyclerContacts.visible()
                    } else {
                        binding.recyclerContacts.gone()
                        binding.addressbookContainerNoContact.visible()
                    }
                    adapter.notifyDataSetChanged()

                }
                is ViewState.Error -> {
                    hideLoading()
                    onMessageToast(it.error?.message)
                }
                else -> {
                }
            }
        })

        viewModel.deleteContactLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Success -> {
                    onSuccessDialog(getString(R.string.contact_deleted_successfully))
                }
                is ViewState.Error -> {
                    hideLoading()
                    onMessageToast(it.error?.message)
                }
                else -> {
                }
            }
        })

        binding.buttonAddContact.setOnClickListener {
            findNavController().navigate(R.id.action_contactFragment_to_contactAddFragment)
        }

        binding.toolbar.getBackBtn().setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.getContactList()
    }


}