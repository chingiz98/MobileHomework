package com.modulstart.mobilehomework.views.transactions

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.modulstart.mobilehomework.views.accounts.accountsList.AccountsRecyclerViewAdapter
import com.modulstart.mobilehomework.views.base.BaseFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_accounts.*
import kotlinx.android.synthetic.main.fragment_transactions.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class TransactionsFragment : BaseFragment(), TransactionsView, TransactionRecyclerAdapterCallback {

    @InjectPresenter
    lateinit var transactionsPresenter: TransactionsPresenter

    @Inject
    lateinit var provider: AccountsRepository

    lateinit var recyclerAdapter : TransactionsRecyclerViewAdapter

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
                        Log.d("MSG", "SELECTED CALENDAR")
                        val builder = MaterialDatePicker.Builder.dateRangePicker()
                        val picker = builder.setTheme(R.style.ThemeOverlay_MaterialComponents_MaterialCalendar).build()
                        picker.show(activity?.supportFragmentManager!!, picker.toString())
                        picker.addOnNegativeButtonClickListener {  }
                        picker.addOnPositiveButtonClickListener {
                            Log.d("MSG", "The selected date range is ${it.first} - ${it.second}")
                            transactionsPresenter.showFiltered(it.first!!, it.second!!)
                        }
                        true
                    }

                    else -> true
                }
            }

        })
        initRecyclerView()
    }

    override fun showData(transactions: MutableList<Transaction>, accounts: MutableList<Account>) {
        setRecyclerAdapter(transactions, accounts)
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