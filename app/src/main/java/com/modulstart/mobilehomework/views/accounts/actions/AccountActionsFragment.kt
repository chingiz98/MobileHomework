package com.modulstart.mobilehomework.views.accounts.actions

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.modulstart.mobilehomework.App
import com.modulstart.mobilehomework.R
import com.modulstart.mobilehomework.presenters.AccountActionsPresenter
import com.modulstart.mobilehomework.repository.accounts.AccountsRepository
import com.modulstart.mobilehomework.repository.database.AppDatabase
import com.modulstart.mobilehomework.repository.database.TemplateDB
import com.modulstart.mobilehomework.repository.models.Account
import com.modulstart.mobilehomework.repository.profile.ProfileRepository
import com.modulstart.mobilehomework.views.base.BaseFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_deposit.view.*
import kotlinx.android.synthetic.main.dialog_template.view.*
import kotlinx.android.synthetic.main.dialog_transfer.view.*
import kotlinx.android.synthetic.main.fragment_account_actions.*
import kotlinx.android.synthetic.main.fragment_accounts.*
import kotlinx.android.synthetic.main.progress_layer.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import java.math.BigDecimal
import javax.inject.Inject


class AccountActionsFragment: BaseFragment(), AccountActionsView {

    @InjectPresenter
    lateinit var actionsPresenter: AccountActionsPresenter

    @Inject
    lateinit var repository: AccountsRepository

    @Inject
    lateinit var profileRepository: ProfileRepository

    lateinit var accounts: MutableList<Account>

    @ProvidePresenter
    fun provideAccountsPresenter(): AccountActionsPresenter {
        val id = arguments?.getLong("accountId")
        return AccountActionsPresenter(repository, profileRepository, id!!)
    }

    override fun onAttach(context: Context) {
        App.appComponent.inject(this)
        super.onAttach(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account_actions, container, false)
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
        showTransferDialog(accounts, null)
    }

    private fun showTransferDialog(accounts: MutableList<Account>, template: TemplateDB?){
        this.accounts = accounts
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.transfer))
        val id = arguments?.getLong("accountId")
        val viewInflated: View = LayoutInflater.from(context).inflate(R.layout.dialog_transfer, view as ViewGroup?, false)
        if(template != null){
            viewInflated.amount.text = Editable.Factory.getInstance().newEditable(template.amount)
            viewInflated.name.text = Editable.Factory.getInstance().newEditable(template.comment)
            viewInflated.manualDest.text = Editable.Factory.getInstance().newEditable(template.toId.toString())
            viewInflated.manualDestCheckbox.isChecked = true
            viewInflated.manualDest.isEnabled = true
            viewInflated.destDropDown.isEnabled = false
        }
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

        builder.setNeutralButton(
            R.string.templates,
            DialogInterface.OnClickListener{ _, _ ->
                actionsPresenter.loadTemplates(id!!)
            }
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

    private fun showTemplateDialog(){

    }

    override fun depositSuccess(amount: BigDecimal) {
        accBal.text = (accBal.text.toString().toBigDecimal() + amount).toString()
        showMessage(getString(R.string.deposit_suc))
    }

    override fun transactionSuccess(fromId: Long, toId: Long, comment: String, amount: BigDecimal) {
        accBal.text = (accBal.text.toString().toBigDecimal() - amount).toString()
        val builder = AlertDialog.Builder(requireContext())
        with(builder)
        {
            setTitle(R.string.warning)
            setMessage(getString(R.string.transaction_suc))
            setPositiveButton("Yes", android.content.DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
                actionsPresenter.saveTemplate(TemplateDB(toId = toId, fromId = fromId, comment = comment, amount = amount.toString()))
            })
            setNegativeButton("No", android.content.DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
            })
            show()
        }
    }


    override fun accountCloseSuccess() {
        findNavController().popBackStack()
    }

    override fun showTemplatesDialog(templates: List<TemplateDB>) {
        var tmp = templates
        val builder = AlertDialog.Builder(requireContext())
        var dialog: AlertDialog? = null
        dialog = with(builder)
        {
            val viewInflated: View = LayoutInflater.from(context).inflate(R.layout.dialog_template, view as ViewGroup?, false)
            viewInflated.templatesList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            viewInflated.templatesList.adapter = TemplatesRecyclerViewAdapter(tmp, object : TemplatesRecyclerViewAdapterCallback {
                override fun onTemplateSelected(template: TemplateDB) {
                    showTransferDialog(accounts, template)
                    dialog?.dismiss()
                }

                override fun onTemplateLongPress(template: TemplateDB) {
                    actionsPresenter.deleteTemplate(template)
                    viewInflated.templatesList.adapter?.notifyDataSetChanged()
                    showTemplatesDialog(tmp.filter { it.id != template.id })
                    dialog?.dismiss()
                }
            })
            if(tmp.isEmpty())
                viewInflated.no_templates_hint.visibility = View.VISIBLE
            setView(viewInflated)
            setTitle(R.string.templates)
            setPositiveButton(getString(R.string.close), android.content.DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
            })
            show()
        }

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