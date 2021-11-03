package com.android.l2l.twolocal.ui.setting.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.l2l.twolocal.databinding.ItemContactBinding;
import com.android.l2l.twolocal.model.AddressBook;

import java.util.List;

import javax.inject.Inject;


public class ContactsRecyclerAdapter extends RecyclerView.Adapter<ContactsRecyclerAdapter.ViewHolder> {

    private final Context context;
    private List<AddressBook> addressBook;
    private Callback callback;

    @Inject
    public ContactsRecyclerAdapter(Context context, List<AddressBook> addressBook, Callback callback) {

        this.context = context;
        this.addressBook = addressBook;
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemContactBinding itemWalletBinding = ItemContactBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(itemWalletBinding);
    }


    public void set(List<AddressBook> items) {
        this.addressBook = items;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AddressBook addressbook = addressBook.get(position);
        holder.bind(addressbook);
    }

    @Override
    public int getItemCount() {
        return addressBook.size();
    }


    public interface Callback {
        void onDeleteClicked(AddressBook addressBook);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemContactBinding binding;

        ViewHolder(ItemContactBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.imageDelete.setOnClickListener(this);
        }

        public void bind(AddressBook wallet) {
            binding.textName.setText(wallet.getName());
            binding.textWalletNumber.setText(wallet.getWallet_number());
            if (wallet.getType() != null)
                binding.textCurrency.setText(wallet.getType().getSymbol());
        }

        @Override
        public void onClick(View v) {
            callback.onDeleteClicked(addressBook.get(getAdapterPosition()));
        }
    }

}
