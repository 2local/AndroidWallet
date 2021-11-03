package com.android.l2l.twolocal.ui.wallet.create;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.android.l2l.twolocal.R;
import com.android.l2l.twolocal.databinding.ItemCreateWalletBinding;
import com.android.l2l.twolocal.model.Wallet;

import java.util.List;


public class CreateWalletAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnItemClickListener mItemClickListener;
    private Context context;
    private List<Wallet> properties;

    public CreateWalletAdapter(Context context, List<Wallet> properties) {
        this.context = context;
        this.properties = properties;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemCreateWalletBinding itemWalletBinding = ItemCreateWalletBinding.inflate(LayoutInflater.from(context), parent, false);
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
        ItemCreateWalletBinding binding;

        WalletViewHolder(ItemCreateWalletBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }

        public void bind(Wallet wallet) {
            binding.txtWalletName.setText(wallet.getType().getMyName());
            binding.imgCoin.setImageResource(wallet.getType().getIcon());
            if (wallet.isSelected()) {
                binding.container.setBackgroundResource(R.drawable.bg_light_gray_border_green_radius_medium);
                binding.imgSelect.setVisibility(View.VISIBLE);
            } else {
                binding.container.setBackgroundResource(R.drawable.bg_light_gray_border_gray_radius_medium);
                binding.imgSelect.setVisibility(View.GONE);
            }

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position == prePosition)
                properties.get(position).setSelected(!properties.get(position).isSelected());
            else {
                for (int i = 0; i < properties.size(); i++) {
                    properties.get(i).setSelected(false);
                }
                properties.get(position).setSelected(true);
            }
            notifyDataSetChanged();
            prePosition = position;
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getLayoutPosition());
            }
        }
    }

    private int prePosition = -1;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

}