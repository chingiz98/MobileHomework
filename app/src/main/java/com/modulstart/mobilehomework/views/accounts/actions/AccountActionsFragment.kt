package com.modulstart.mobilehomework.views.accounts.actions

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.modulstart.mobilehomework.App
import com.modulstart.mobilehomework.R
import com.modulstart.mobilehomework.presenters.AccountActionsPresenter
import com.modulstart.mobilehomework.repository.accounts.AccountsRepository
import com.modulstart.mobilehomework.repository.models.Account
import com.modulstart.mobilehomework.views.base.BaseFragment
import kotlinx.android.synthetic.main.dialog_deposit.view.*
import kotlinx.android.synthetic.main.fragment_account_actions.*
import kotlinx.android.synthetic.main.progress_layer.*
import kotlinx.android.synthetic.main.dialog_transfer.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import java.math.BigDecimal
import javax.inject.Inject


class AccountActionsFragment: BaseFragment(), AccountActionsView {

    @InjectPresenter
    lateinit var actionsPresenter: AccountActionsPresenter

    @Inject
    lateinit var repository: AccountsRepository



    @ProvidePresenter
    fun provideAccountsPresenter(): AccountActionsPresenter {
        val id = arguments?.getLong("accountId")
        return AccountActionsPresenter(repository, id!!)
    }

    override fun onAttach(context: Context) {
        App.appComponent.inject(this)
        super.onAttach(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_account_actions, container, false)
        //actionsPresenter.loadAccount()

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initButtons()
    }


    override fun showLoading() {
        progressLayer.visibility = View.VISIBLE
    }

    override fun showAccount(account: Account) {
        accName.text = account.name
        accNum.text = account.id.toString()
        accBal.text = account.amount.toString()
        progressLayer.visibility = View.GONE
    }

    override fun showTransferDialog(accounts: MutableList<Account>) {

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.transfer))
        val id = arguments?.getLong("accountId")
        val viewInflated: View = LayoutInflater.from(context).inflate(R.layout.dialog_transfer, view as ViewGroup?, false)
        val spinner: Spinner = viewInflated.destDropDown
        ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, accounts.filter { it.id != id }.map { it.id }).also {
            arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = arrayAdapter
        }
        viewInflated.manualDestCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            viewInflated.manualDest.isEnabled = isChecked
            viewInflated.destDropDown.isEnabled = !isChecked
        }

        builder.setView(viewInflated)
        builder.setPositiveButton(
            android.R.string.ok,
            DialogInterface.OnClickListener { _, _ ->

            }
        )
        builder.setNegativeButton(
            android.R.string.cancel,
            DialogInterface.OnClickListener{ _, _ -> }
        )

        val dialog: AlertDialog = builder.create()
        dialog.setOnShowListener {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {

                var error = false
                if(viewInflated.amount.text.toString().isEmpty()){
                    viewInflated.amountLayout.error = getString(R.string.amount_error)
                    error = true
                }
                else {
                    viewInflated.amountLayout.error = null
                }
                if(viewInflated.manualDestCheckbox.isChecked && viewInflated.manualDest.text.toString().isEmpty()){
                    viewInflated.manualDestLayout.error = getString(R.string.amount_error)
                    error = true
                }
                else {
                    viewInflated.manualDestLayout.error = null
                }
                if(!error){
                    val id = arguments?.getLong("accountId")
                    val toId = if(viewInflated.manualDestCheckbox.isChecked) viewInflated.manualDest.text.toString().toLong() else viewInflated.destDropDown.selectedItem.toString().toLong()
                    actionsPresenter.makeTransaction(id!!, toId, viewInflated.amount.text.toString().toBigDecimal(), viewInflated.name.text.toString())
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
        dialog.setCancelable(false)
    }

    override fun depositSuccess(amount: BigDecimal) {
        accBal.text = (accBal.text.toString().toBigDecimal() + amount).toString()
        showMessage(getString(R.string.deposit_suc))
    }

    override fun transactionSuccess(amount: BigDecimal) {
        accBal.text = (accBal.text.toString().toBigDecimal() - amount).toString()
        showMessage(getString(R.string.transaction_suc))
    }

    override fun accountCloseSuccess() {
        findNavController().popBackStack()
    }

    private fun showMessage(msg: String) {
        val builder = AlertDialog.Builder(requireContext())
        with(builder)
        {
            setTitle(R.string.warning)
            setMessage(msg)
            setPositiveButton("OK", android.content.DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
            })
            show()
        }
    }

    private fun initButtons() {
        closeButton.setOnClickListener {
            MaterialAlertDialogBuilder(context)
                .setTitle(resources.getString(R.string.warning))
                .setMessage(resources.getString(R.string.close_acc_warning))
                .setNegativeButton(resources.getString(R.string.no)) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                    actionsPresenter.closeAccount(arguments?.getLong("accountId")!!)
                }
                .show()
        }
        depositButton.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setTitle(getString(R.string.deposit))

            val viewInflated: View = LayoutInflater.from(context).inflate(R.layout.dialog_deposit, view as ViewGroup?, false)

            builder.setView(viewInflated)
            builder.setPositiveButton(
                android.R.string.ok,
                DialogInterface.OnClickListener { _, _ -> }
            )
            builder.setNegativeButton(
                android.R.string.cancel,
                DialogInterface.OnClickListener { dialog, _ -> dialog.cancel() }
            )
            val layoutParams = WindowManager.LayoutParams()
            val dialog: AlertDialog = builder.create()

            dialog.setCancelable(false)
            dialog.setOnShowListener {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                    val id = arguments?.getLong("accountId")
                    if(viewInflated.input.text!!.isNotEmpty()){
                        actionsPresenter.makeDeposit(id!!, viewInflated.input.text.toString().toBigDecimal())
                        dialog.dismiss()
                    } else {
                        viewInflated.depositLayout.error = getString(R.string.deposit_error)
                    }
                }
            }
            dialog.show()
            val displayMetrics = DisplayMetrics()

            activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
            val displayWidth = displayMetrics.widthPixels
            val dialogWindowWidth = (displayWidth * 0.7f).toInt()
            layoutParams.copyFrom(dialog.window!!.attributes)
            layoutParams.width = dialogWindowWidth
            dialog.window!!.attributes =layoutParams
        }
        transactionIcon.setOnClickListener {
            actionsPresenter.loadAccounts()
        }
    }
}