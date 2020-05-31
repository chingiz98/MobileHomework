package com.modulstart.mobilehomework.views.transactions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.modulstart.mobilehomework.R
import com.modulstart.mobilehomework.repository.models.Account
import com.modulstart.mobilehomework.repository.models.Transaction
import kotlinx.android.synthetic.main.transaction_item.view.*
import java.text.SimpleDateFormat
import java.util.*


class TransactionsRecyclerViewAdapter(
    var transactions:MutableList<Transaction>,
    var accounts:MutableList<Account>,
    var callback: TransactionRecyclerAdapterCallback
) : RecyclerView.Adapter<TransactionsRecyclerViewAdapter.TransactionsViewHolder>() {

    inner class TransactionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(pos:Int)
        {
            val transaction = transactions[pos]

            val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.ROOT)

            val currentDateandTime: String = sdf.format(transaction.timestamp)

            itemView.date.text = currentDateandTime
            if(transaction.type == "transaction"){
                itemView.opType.text = itemView.context.getString(R.string.transfer)
                itemView.toTransaction.text = itemView.context.getString(R.string.transaction_to, transaction.toId)
                itemView.fromTransaction.text = itemView.context.getString(R.string.transaction_from, transaction.fromId)
                if(accounts.find { it.id == transaction.toId } != null && accounts.find { it.id == transaction.fromId } != null){
                    itemView.transactionIcon.setImageResource(R.drawable.ic_swap_vert_black_48dp)
                    itemView.amount.text = itemView.context.getString(R.string.transaction_swap, transaction.amount.toString())
                    itemView.amount.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.black))
                }

                if(accounts.find { it.id == transaction.toId } != null && accounts.find { it.id == transaction.fromId } == null){
                    itemView.transactionIcon.setImageResource(R.drawable.ic_arrow_circle_down_black_48dp)
                    itemView.amount.text = itemView.context.getString(R.string.transaction_plus, transaction.amount)
                    itemView.amount.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorGreen))
                }

                if(accounts.find { it.id == transaction.toId } == null && accounts.find { it.id == transaction.fromId } != null){
                    itemView.transactionIcon.setImageResource(R.drawable.ic_arrow_circle_up_black_48dp)
                    itemView.amount.text = itemView.context.getString(R.string.transaction_minus, transaction.amount.toString())
                    itemView.amount.setTextColor(ContextCompat.getColor(itemView.context, R.color.logoutColor))
                }
            }

            if(transaction.type == "deposit"){
                itemView.opType.text = itemView.context.getString(R.string.deposit)
                itemView.toTransaction.text = itemView.context.getString(R.string.transaction_to, transaction.toId)
                itemView.fromTransaction.text = itemView.context.getString(R.string.transaction_from_deposit)
                itemView.amount.text = itemView.context.getString(R.string.transaction_plus, transaction.amount.toString())
                itemView.amount.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorGreen))
                itemView.transactionIcon.setImageResource(R.drawable.ic_arrow_circle_down_black_48dp)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionsViewHolder {
        return TransactionsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.transaction_item,
                parent,
                false
            ))
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onBindViewHolder(holder: TransactionsViewHolder, position: Int) {
        holder.bind(position)
        holder.itemView.setOnClickListener {
            callback.onTransactionSelected(transactions[position])
        }
    }
}

interface TransactionRecyclerAdapterCallback {
    fun onTransactionSelected(transaction: Transaction)
}