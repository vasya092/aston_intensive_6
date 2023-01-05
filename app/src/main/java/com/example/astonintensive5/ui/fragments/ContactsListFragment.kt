package com.example.astonintensive5.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.example.astonintensive5.R
import com.example.astonintensive5.databinding.FragmentContactsListBinding
import com.example.astonintensive5.model.Contact
import com.example.astonintensive5.ui.adapter.ContactsListAdapter
import com.example.astonintensive5.ui.ContactsListOnBackPressure
import com.example.astonintensive5.data.ContactsViewModel
import com.google.android.material.color.MaterialColors.layer


class ContactsListFragment : Fragment() {

    private val contactsViewModel: ContactsViewModel by activityViewModels()
    private lateinit var slidingPaneLayout: SlidingPaneLayout
    private lateinit var adapter: ContactsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.fragment_contacts_list, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ContactsListAdapter(onItemClicked = {
            openContactDetailFragment(it)
        }, onItemLongClicked = {
            showConfirmWindow(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentContactsListBinding.bind(view)
        slidingPaneLayout = binding.slidingPaneLayout
        setupPaneLayout()

        contactsViewModel.usersData.observe(viewLifecycleOwner) { list ->
            adapter.updateContactList(list)
            if (contactsViewModel.filterList.isNotEmpty()) {
                adapter.submitList(contactsViewModel.filterList)
            }
        }

        binding.searchEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filterListByFullName(s.toString())
            }
        })

        binding.contactsList.adapter = adapter

        contactsViewModel.isContactSelected.observe(this.viewLifecycleOwner) { isContactSelected ->
            if (!isContactSelected) {
                slidingPaneLayout.closePane()
            }
        }

        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.divider_decorator, null))
        binding.contactsList.addItemDecoration(dividerItemDecoration)

        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
    }

    private fun showConfirmWindow(contact: Contact) {
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.contacts_list_delet_item_string))
            .setMessage(getString(R.string.contacts_list_delete_item_description))
            .setPositiveButton(R.string.alert_positive_button) { _, _ ->
                contactsViewModel.onContactRemoveListener(contact)
            }
            .setNegativeButton(R.string.alert_negative_button, null)
            .show()
    }

    private fun filterListByFullName(name: String) {
        contactsViewModel.filterList = mutableListOf()
        contactsViewModel.usersData.value?.forEach { contact ->
            if (contact.name.lowercase().contains(name.lowercase()) || contact.surname.lowercase().contains(name)) {
                contactsViewModel.filterList.add(contact)
            }
        }
        adapter.submitList(contactsViewModel.filterList)
    }

    private fun selectContact(contact: Contact) {
        contactsViewModel.onCurrentContactChange(contact)
        contactsViewModel.onSelectedContactStateChange(true)
    }

    private fun openContactDetailFragment(contact: Contact) {
        selectContact(contact)
        childFragmentManager.commit {
            setReorderingAllowed(true)
            replace<ContactDetailFragment>(R.id.contact_detail_container)
            if (slidingPaneLayout.isOpen) {
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            }
        }
        slidingPaneLayout.open()
    }

    private fun setupPaneLayout() {
        slidingPaneLayout.lockMode = SlidingPaneLayout.LOCK_MODE_UNLOCKED
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            ContactsListOnBackPressure(slidingPaneLayout)
        )
    }

    override fun onResume() {
        super.onResume()
        slidingPaneLayout.closePane()
    }
}