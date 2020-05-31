package com.modulstart.mobilehomework.views.accounts.accountsList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.modulstart.mobilehomework.R
import com.modulstart.mobilehomework.repository.models.Account
import kotlinx.android.synthetic.main.account_item.view.*

class AccountsRecyclerViewAdapter(
    var accounts:MutableList<Account>,
    var callback: AccountsRecyclerAdapterCallback
) : RecyclerView.Adapter<AccountsRecyclerViewAdapter.AccountViewHolder>() {

    inner class AccountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(pos:Int)
        {
            itemView.accName.text = accounts[pos].name
            itemView.accNum.text = accounts[pos].id.toString()
            itemView.accBal.text = accounts[pos].amount.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        return AccountViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.account_item,
                parent,
                false
            ))
    }

    override fun getItemCount(): Int {
        return accounts.size
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bind(position)
        holder.itemView.setOnClickListener {
            callback.onAccountSelected(accounts[position])
        }
    }
}

interface AccountsRecyclerAdapterCallback {
    fun onAccountSelected(account: Account)
}