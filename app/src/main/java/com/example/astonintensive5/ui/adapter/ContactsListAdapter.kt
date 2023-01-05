package com.example.astonintensive5.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.astonintensive5.R
import com.example.astonintensive5.databinding.ContactsListItemBinding
import com.example.astonintensive5.model.Contact
import com.squareup.picasso.Picasso

class ContactsListAdapter(
    private val onItemClicked: (Contact) -> Unit,
    private val onItemLongClicked: (Contact) -> Unit,
) : ListAdapter<Contact, ContactsListAdapter.ContactsListViewHolder>(ContactsDiffCallback) {

    class ContactsListViewHolder(private var binding: ContactsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact, context: Context) {
            with(binding) {
                contactFullName.text =
                    root.context.getString(R.string.contact_item_full_name_template,
                        contact.name,
                        contact.surname)
                contactNumber.text = contact.number
                Picasso.get().load(contact.imageUrl)
                    .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                    .error(android.R.drawable.stat_notify_error)
                    .into(contactPhoto)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsListViewHolder {
        return ContactsListViewHolder(
            ContactsListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ContactsListViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(currentItem)
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClicked(currentItem)
            true
        }
        holder.bind(currentItem, holder.itemView.context)
    }

    fun updateContactList(contactList: List<Contact>) {
        submitList(contactList)
        notifyDataSetChanged()
    }
}

object ContactsDiffCallback : DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem.name == newItem.name
    }
}