package com.modulstart.mobilehomework.views.accounts.actions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.modulstart.mobilehomework.R
import com.modulstart.mobilehomework.repository.database.TemplateDB
import com.modulstart.mobilehomework.repository.models.Account
import kotlinx.android.synthetic.main.account_item.view.*
import kotlinx.android.synthetic.main.template_item.view.*

class TemplatesRecyclerViewAdapter(
    var teamplates:List<TemplateDB>,
    var callback: TemplatesRecyclerViewAdapterCallback
) : RecyclerView.Adapter<TemplatesRecyclerViewAdapter.TemplateViewHolder>() {

    inner class TemplateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(pos:Int)
        {
            itemView.toTransaction.text = teamplates[pos].toId.toString()
            itemView.templateName.text = teamplates[pos].comment.toString()
            if(teamplates[pos].comment.toString().isEmpty())
                itemView.templateName.text = itemView.context.getString(R.string.template_no_name)
            itemView.amount.text = teamplates[pos].amount.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemplateViewHolder {
        return TemplateViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.template_item,
                parent,
                false
            ))
    }

    override fun getItemCount(): Int {
        return teamplates.size
    }

    override fun onBindViewHolder(holder: TemplateViewHolder, position: Int) {
        holder.bind(position)
        holder.itemView.setOnClickListener {
            callback.onTemplateSelected(teamplates[position])
        }
        holder.itemView.setOnLongClickListener {
            callback.onTemplateLongPress(teamplates[position])
            true
        }
    }
}

interface TemplatesRecyclerViewAdapterCallback {
    fun onTemplateSelected(template: TemplateDB)
    fun onTemplateLongPress(template: TemplateDB)
}