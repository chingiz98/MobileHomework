package com.modulstart.mobilehomework.views.transactions

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.*
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.modulstart.mobilehomework.App
import com.modulstart.mobilehomework.R
import com.modulstart.mobilehomework.presenters.TransactionsPresenter
import com.modulstart.mobilehomework.repository.accounts.AccountsRepository
import com.modulstart.mobilehomework.repository.models.Account
import com.modulstart.mobilehomework.repository.models.Transaction
import com.modulstart.mobilehomework.views.base.BaseFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_accounts.*
import kotlinx.android.synthetic.main.fragment_transactions.*
import kotlinx.android.synthetic.main.progress_layer.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class TransactionsFragment : BaseFragment(), TransactionsView, TransactionRecyclerAdapterCallback {

    @InjectPresenter
    lateinit var transactionsPresenter: TransactionsPresenter

    @Inject
    lateinit var provider: AccountsRepository

    private lateinit var recyclerAdapter : TransactionsRecyclerViewAdapter
    private lateinit var picker : MaterialDatePicker<androidx.core.util.Pair<Long, Long>>

    @ProvidePresenter
    fun provideAccountsPresenter(): TransactionsPresenter {
        return TransactionsPresenter(provider)
    }


    override fun onAttach(context: Context) {
        App.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_transactions, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.toolbar?.inflateMenu(R.menu.tranactions_menu)
        activity?.toolbar?.setOnMenuItemClickListener(object : Toolbar.OnMenuItemClickListener,
            androidx.appcompat.widget.Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                return when (item!!.itemId) {
                    R.id.calendar_view -> {
                        picker.show(activity?.supportFragmentManager!!, picker.toString())
                        true
                    }

                    else -> true
                }
            }

        })
        initRecyclerView()
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        picker = builder.build()
        picker.addOnNegativeButtonClickListener {  }
        picker.addOnPositiveButtonClickListener {
            transactionsPresenter.filter(it.first!!, it.second!!)
        }
    }

    override fun showTransactionsLoading() {
        progressLayer.visibility = View.VISIBLE
    }

    override fun showData(transactions: MutableList<Transaction>, accounts: MutableList<Account>) {
        setRecyclerAdapter(transactions, accounts)
        progressLayer.visibility = View.GONE
        if(transactions.isEmpty())
            no_transactions_hint.visibility = View.VISIBLE
        else
            no_transactions_hint.visibility = View.GONE
    }



    private fun setRecyclerAdapter(transactions: MutableList<Transaction>, accounts: MutableList<Account>) {
        recyclerAdapter = TransactionsRecyclerViewAdapter(transactions, accounts, this)
        transactionsList.adapter = recyclerAdapter
    }

    override fun onTransactionSelected(transaction: Transaction) {

    }

    private fun initRecyclerView() {
        val horizontalLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        transactionsList.layoutManager = horizontalLayoutManager
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }
}