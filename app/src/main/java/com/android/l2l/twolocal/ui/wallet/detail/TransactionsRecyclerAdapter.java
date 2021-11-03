package com.android.l2l.twolocal.ui.wallet.detail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.l2l.twolocal.R;
import com.android.l2l.twolocal.databinding.ItemTransactionsHistoryBinding;
import com.android.l2l.twolocal.model.WalletTransactionHistory;

import java.util.List;

import static com.android.l2l.twolocal.utils.constants.AppConstants.TRANSACTION_SUCCESS;


public class TransactionsRecyclerAdapter extends RecyclerView.Adapter<TransactionsRecyclerAdapter.ViewHolder> {

    private final Context context;
    private List<WalletTransactionHistory> transactionsList;
    private OnItemClickListener mItemClickListener;

    public TransactionsRecyclerAdapter(Context context, List<WalletTransactionHistory> transactionsList) {
        this.context = context;
        this.transactionsList = transactionsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTransactionsHistoryBinding itemWalletBinding = ItemTransactionsHistoryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(itemWalletBinding);
    }


    public void set(List<WalletTransactionHistory> items) {
        transactionsList.clear();
        transactionsList.addAll(items);
        notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WalletTransactionHistory transactions = transactionsList.get(position);
        holder.bind(transactions);
    }

    @Override
    public int getItemCount() {
        return transactionsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemTransactionsHistoryBinding binding;

        ViewHolder(ItemTransactionsHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        public void bind(WalletTransactionHistory transactions) {
            binding.textTransactionType.setText(transactions.getTransactionTransferTypeStringResource());
            binding.textDate.setText(transactions.getDate());

            binding.textAmount.setText(String.format("%s %s", transactions.getValuePriceFormatted(), transactions.getType().getSymbol()));
            binding.textStatus.setText(context.getString(transactions.getTxreceiptStatusString()));

            if (transactions.isSend()) {
                binding.imgTransaction.setImageResource(R.drawable.ic_transaction_sent);
                binding.textAmount.setTextColor(ContextCompat.getColor(context, R.color.red));
            }
            else {
                binding.imgTransaction.setImageResource(R.drawable.ic_transaction_receive);
                binding.textAmount.setTextColor(ContextCompat.getColor(context, R.color.green));
            }

            if (transactions.getTxreceiptStatus().equals(TRANSACTION_SUCCESS))
                binding.textStatus.setTextColor(ContextCompat.getColor(context, R.color.green));
            else
                binding.textStatus.setTextColor(ContextCompat.getColor(context, R.color.red));


        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener!=null)
                mItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

}
