package com.android.l2l.twolocal.ui.wallet.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.android.l2l.twolocal.R;
import com.android.l2l.twolocal.databinding.ItemHomeAddWalletBinding;
import com.android.l2l.twolocal.databinding.ItemHomeWalletBinding;
import com.android.l2l.twolocal.model.Wallet;
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType;

import java.util.List;


public class WalletHomeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnItemClickListener mItemClickListener;
    private Context context;
    private List<Wallet> properties;

    public WalletHomeRecyclerViewAdapter(Context context, List<Wallet> properties) {
        this.context = context;
        this.properties = properties;
    }

    @Override
    public int getItemViewType(int position) {
        return properties.get(position).getType() == CryptoCurrencyType.NONE ? 2 : 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                ItemHomeWalletBinding itemWalletBinding = ItemHomeWalletBinding.inflate(LayoutInflater.from(context), parent, false);
                return new WalletViewHolder(itemWalletBinding);
            case 2:
                ItemHomeAddWalletBinding itemAddWalletBinding = ItemHomeAddWalletBinding.inflate(LayoutInflater.from(context), parent, false);
                return new AddWalletViewHolder(itemAddWalletBinding);
        }
        ItemHomeAddWalletBinding itemAddWalletBinding = ItemHomeAddWalletBinding.inflate(LayoutInflater.from(context), parent, false);
        return new AddWalletViewHolder(itemAddWalletBinding);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Wallet wallet = properties.get(position);
        if (holder instanceof WalletViewHolder) {
            WalletViewHolder h = (WalletViewHolder) holder;
            h.bind(wallet);
        } else if (holder instanceof AddWalletViewHolder) {
            AddWalletViewHolder h = (AddWalletViewHolder) holder;
        }
    }


    @Override
    public int getItemCount() {
        return properties.size();
    }


    public class WalletViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemHomeWalletBinding binding;

        WalletViewHolder(ItemHomeWalletBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.containerWallet.setOnClickListener(this);
            binding.btnReceive.setOnClickListener(this);
            binding.btnSend.setOnClickListener(this);
            binding.btnBuy.setOnClickListener(this);
        }

        public void bind(Wallet wallet) {
//            if (wallet.isCanBuy())
//                binding.btnBuy.setVisibility(View.VISIBLE);
//            else
//                binding.btnBuy.setVisibility(View.GONE);
            binding.txtTokenSymbol.setText(wallet.getType().getSymbol());
            binding.imgCoin.setImageResource(wallet.getType().getIcon());

            if (!wallet.isShowAmount()) {
                binding.txtTokenAmount.setText(R.string.hidden_stars);
                binding.txtUSDAmount.setText(R.string.hidden_stars);
            } else {
                binding.txtUSDAmount.setText(String.format("%s %s", wallet.getCurrency().getMySymbol(), wallet.getFiatPriceFormat()));
                binding.txtTokenAmount.setText(wallet.getAmountPriceFormat());
            }
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();


            if (mItemClickListener != null)
                mItemClickListener.onItemClick(v, getLayoutPosition());

        }
    }


    public class AddWalletViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemHomeAddWalletBinding binding;

        AddWalletViewHolder(ItemHomeAddWalletBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.containerAddWallet.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (mItemClickListener != null)
                mItemClickListener.onItemClick(v, getLayoutPosition());
//            CreateWalletActivity.start(context);
        }
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}