package com.android.l2l.twolocal.ui.wallet.myWalletList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.android.l2l.twolocal.databinding.ItemMyWalletBinding;
import com.android.l2l.twolocal.model.Wallet;

import java.util.List;


public class MyWalletRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Wallet> properties;
    private OnItemClickListener callback;

    public MyWalletRecyclerViewAdapter(Context context, List<Wallet> properties, OnItemClickListener callback) {
        this.context = context;
        this.properties = properties;
        this.callback = callback;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemMyWalletBinding itemWalletBinding = ItemMyWalletBinding.inflate(LayoutInflater.from(context), parent, false);
        return new WalletViewHolder(itemWalletBinding);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Wallet wallet = properties.get(position);
        if (holder instanceof WalletViewHolder) {
            WalletViewHolder h = (WalletViewHolder) holder;
            h.bind(wallet);
        }
    }


    @Override
    public int getItemCount() {
        return properties.size();
    }


    public class WalletViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemMyWalletBinding binding;

        WalletViewHolder(ItemMyWalletBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }

        public void bind(Wallet wallet) {
            binding.txtWalletName.setText(wallet.getWalletName());
            binding.txtTokenSymbol.setText(wallet.getType().getSymbol());
            binding.txtTokenAmount.setText(wallet.getAmountPriceFormat());
            binding.txtCurrencyAmount.setText(String.format("%s%s", wallet.getCurrency().getMySymbol(), wallet.getFiatPriceFormat()));
            binding.imgCoin.setImageResource(wallet.getType().getIcon());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            if (callback != null) {
                callback.onItemClick(v, getLayoutPosition());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}