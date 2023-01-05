package com.example.astonintensive5.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.astonintensive5.R
import com.example.astonintensive5.databinding.FragmentContactDetailBinding
import com.example.astonintensive5.model.Contact
import com.example.astonintensive5.data.ContactsViewModel
import com.example.astonintensive5.utils.hideKeyboard
import com.example.astonintensive5.utils.makeToast

class ContactDetailFragment : Fragment() {

    private val contactsViewModel: ContactsViewModel by activityViewModels()
    private lateinit var binding: FragmentContactDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentContactDetailBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentContactDetailBinding.bind(view)
        hideContactDetails()

        contactsViewModel.currentContact.observe(this.viewLifecycleOwner) { contact ->
            onContactUpdate(contact)
        }

        binding.detailContactSaveButton.setOnClickListener {
            onSaveButtonClick()
        }
    }

    private fun onSaveButtonClick() {
        with(binding) {
            if (areFieldsFilled()) {
                contactsViewModel.onContactDataChange(Contact(
                    contactsViewModel.currentContact.value?.id ?: 0,
                    detailContactName.text.toString(),
                    detailContactSurname.text.toString(),
                    detailContactNumber.text.toString()
                ))
                requireContext().hideKeyboard(root.windowToken)
                contactsViewModel.onSelectedContactStateChange(false)
            } else {
                requireContext().makeToast(getString(R.string.fields_must_be_filled_error))
            }
        }
    }

    private fun onContactUpdate(contact: Contact) {
        showContactDetails()
        with(binding) {
            detailContactName.setText(contact.name)
            detailContactSurname.setText(contact.surname)
            detailContactNumber.setText(contact.number)
        }
    }

    private fun areFieldsFilled(): Boolean {
        with(binding) {
            return !detailContactName.text.isNullOrBlank() && !detailContactSurname.text.isNullOrBlank() && !detailContactNumber.text.isNullOrBlank()
        }
    }

    private fun showContactDetails() {
        binding.root.visibility = View.VISIBLE
    }

    private fun hideContactDetails() {
        binding.root.visibility = View.GONE
    }
}