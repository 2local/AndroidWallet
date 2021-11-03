package com.android.l2l.twolocal.ui.wallet.send;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.l2l.twolocal.R;
import com.android.l2l.twolocal.model.AddressBook;

import java.util.List;

import javax.inject.Inject;

public class AddressBookSpinnerAdapter extends BaseAdapter {

    private final Context context;
    private List<AddressBook> items;


    public AddressBookSpinnerAdapter(Context context, List<AddressBook> items) {

        this.context = context;
        this.items = items;
    }

    public void set(List<AddressBook> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        @SuppressLint("ViewHolder")
        View row = LayoutInflater.from(context).inflate(R.layout.item_spinner_addressbook, parent, false);
        TextView text_name = row.findViewById(R.id.text_name);
        TextView text_walletNumber = row.findViewById(R.id.text_walletNumber);
        ConstraintLayout container = row.findViewById(R.id.container);
        text_name.setText(items.get(position).getName());
        text_walletNumber.setText(items.get(position).getWallet_number());
        return row;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder")
        View row = LayoutInflater.from(context).inflate(R.layout.item_spinner_addressbook, viewGroup, false);
        TextView text_name = row.findViewById(R.id.text_name);
        TextView text_walletNumber = row.findViewById(R.id.text_walletNumber);
        text_name.setText(items.get(i).getName());
        text_walletNumber.setText(items.get(i).getWallet_number());
        return row;
    }

}
