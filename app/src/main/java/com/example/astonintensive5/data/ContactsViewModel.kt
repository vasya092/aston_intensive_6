package com.example.astonintensive5.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.astonintensive5.model.Contact
import io.github.serpro69.kfaker.Faker

class ContactsViewModel : ViewModel() {

    private val _usersData: MutableLiveData<MutableList<Contact>> =
        MutableLiveData<MutableList<Contact>>(
            mutableListOf())
    val usersData: LiveData<MutableList<Contact>>
        get() = _usersData

    var filterList: MutableList<Contact> = mutableListOf()

    var prevUsersData: List<Contact> = listOf()

    private var _currentContact: MutableLiveData<Contact> = MutableLiveData<Contact>()
    val currentContact: LiveData<Contact>
        get() = _currentContact

    private var _isContactSelected: MutableLiveData<Boolean> = MutableLiveData(false)
    val isContactSelected: LiveData<Boolean> = _isContactSelected

    init {
        val faker = Faker()
        val generatedUsers = (1..100).map {
            Contact(
                id = it,
                name = faker.name.firstName(),
                surname = faker.name.lastName(),
                number = faker.phoneNumber.phoneNumber(),
                imageUrl = "https://source.unsplash.com/random/200x200?sig=${it}"
            )
        }
        _usersData.value = generatedUsers.toMutableList()
        prevUsersData = generatedUsers
    }

    fun onSelectedContactStateChange(isSelected: Boolean) {
        _isContactSelected.value = isSelected
    }

    fun onCurrentContactChange(contact: Contact) {
        _currentContact.value = contact
    }

    fun onContactDataChange(contact: Contact) {
        val currentContactsData = usersData.value
        if (currentContactsData != null) {
            val changeIndex = currentContactsData.indexOfFirst { it.id == contact.id }

            with(currentContactsData[changeIndex]) {
                name = contact.name
                surname = contact.surname
                number = contact.number
            }
            _usersData.value = currentContactsData ?: mutableListOf()
        }
    }

    fun onContactRemoveListener(contact: Contact) {
        val currentContactData = usersData.value?.toMutableList()
        if (currentContactData != null) {
            val changeIndex = currentContactData.indexOfFirst { it.id == contact.id }
            currentContactData.removeAt(changeIndex)
        }
        _usersData.value = currentContactData?.toMutableList()
    }
}