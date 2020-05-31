package com.modulstart.mobilehomework.views.accounts.accountsList


import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.modulstart.mobilehomework.App
import com.modulstart.mobilehomework.R
import com.modulstart.mobilehomework.presenters.AccountsPresenter
import com.modulstart.mobilehomework.repository.accounts.AccountsRepository
import com.modulstart.mobilehomework.repository.models.Account
import com.modulstart.mobilehomework.views.base.BaseFragment
import kotlinx.android.synthetic.main.dialog_create_acc.view.*
import kotlinx.android.synthetic.main.dialog_deposit.view.*
import kotlinx.android.synthetic.main.dialog_deposit.view.input
import kotlinx.android.synthetic.main.fragment_accounts.*
import kotlinx.android.synthetic.main.progress_layer.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject


class AccountsFragment : BaseFragment(),
    AccountsView,
    AccountsRecyclerAdapterCallback {

    @InjectPresenter
    lateinit var accountsPresenter: AccountsPresenter

    @Inject
    lateinit var provider: AccountsRepository

    lateinit var recyclerAdapter : AccountsRecyclerViewAdapter

    lateinit var accounts: MutableList<Account>

    @ProvidePresenter
    fun provideAccountsPresenter(): AccountsPresenter {
        return AccountsPresenter(provider)
    }

    override fun onAttach(context: Context) {
        App.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_accounts, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        addNewAccBtn.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setTitle(getString(R.string.acc_create))

            val viewInflated: View = LayoutInflater.from(context).inflate(R.layout.dialog_create_acc, view as ViewGroup?, false)

            builder.setView(viewInflated)
            builder.setPositiveButton(
                android.R.string.ok,
                DialogInterface.OnClickListener { dialog, which ->

                }
            )
            builder.setNegativeButton(
                android.R.string.cancel,
                DialogInterface.OnClickListener { dialog, which -> dialog.cancel() }
            )
            val layoutParams = WindowManager.LayoutParams()
            val dialog: AlertDialog = builder.create()

            dialog.setCancelable(false)

            dialog.setOnShowListener {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {

                    if(viewInflated.input.text!!.isNotEmpty()){
                        accountsPresenter.createAccount(viewInflated.input.text.toString())
                        dialog.dismiss()
                    } else {
                        viewInflated.createLayout.error = getString(R.string.acc_no_name_error)
                    }

                }
            }
            dialog.show()
            val displayMetrics = DisplayMetrics()

            activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
            val displayWidth = displayMetrics.widthPixels
            val displayHeight = displayMetrics.heightPixels
            val dialogWindowWidth = (displayWidth * 0.7f).toInt()
            val dialogWindowHeight = (displayHeight * 0.5f).toInt()

            layoutParams.copyFrom(dialog.window!!.attributes)
            layoutParams.width = dialogWindowWidth
            dialog.window!!.attributes =layoutParams

        }
    }

    override fun showAccountsLoading() {
        Log.d("MSG", "Accounts loading")
        progressLayer.visibility = View.VISIBLE
    }

    override fun showAccounts(accounts: MutableList<Account>) {
        Log.d("MSG", "Accounts loaded")
        progressLayer.visibility = View.GONE
        if(accounts.isEmpty())
            no_accounts_hint.visibility = View.VISIBLE
        else
            no_accounts_hint.visibility = View.GONE
        setRecyclerAdapter(accounts)
    }

    override fun createAccountSuccess(account: Account) {
        //accounts.add(0, account)
        recyclerAdapter.notifyItemInserted(0)
        accountsList.scrollToPosition(0)
    }


    private fun setRecyclerAdapter(accounts: MutableList<Account>) {
        this.accounts = accounts
        recyclerAdapter =
            AccountsRecyclerViewAdapter(
                accounts,
                this
            )
        accountsList.adapter = recyclerAdapter
    }

    override fun onAccountSelected(account: Account) {
        Log.d("MSG", "Account selected")
        val bundle = Bundle()
        bundle.putLong("accountId", account.id)
        findNavController().navigate(R.id.action_accounts_fragment_to_accountActionsFragment, bundle)

    }

    private fun initRecyclerView() {
        val horizontalLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        accountsList.layoutManager = horizontalLayoutManager
        accountsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if(dy > 0){
                    addNewAccBtn.hide();
                } else {
                    addNewAccBtn.show();
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

}
