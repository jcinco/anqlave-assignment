package com.jcinco.j5anqlaveassignment.views.browser.list

import android.content.Context
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jcinco.j5anqlaveassignment.R
import com.jcinco.j5anqlaveassignment.data.model.file.FileInfo
import com.jcinco.j5anqlaveassignment.databinding.ItemFileBinding
import info.androidhive.fontawesome.FontDrawable
import kotlinx.android.synthetic.main.item_file.view.*

class FileAdapter(
    private val files: ArrayList<FileInfo>,
    var itemClickListener: (fileInfo: FileInfo, action:Int)->Unit?
): RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    lateinit var context: Context

    override fun getItemCount(): Int = files.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        this.context = parent.context
        return FileViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_file,
                parent,
                false
            ),
            itemClickListener
        )
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val fileInfo = files[holder.adapterPosition]
        var iconRef = if (fileInfo.isDir == true) R.string.fa_folder else R.string.fa_file
        var iconColor = if (fileInfo.isDir == true) R.color.yellow else R.color.colorPrimaryDark
        if (fileInfo.isEncrypted)
            iconRef = R.string.fa_file_archive
        val iconDrawable = FontDrawable(context, iconRef, false, false)
        iconDrawable.setTextColor(iconColor)
        iconDrawable.textSize = 30f
        holder.itemFileBinding.fileInfo = fileInfo
        holder.itemView.fileIcon.setImageDrawable(iconDrawable)
    }


    inner class FileViewHolder(
        val itemFileBinding: ItemFileBinding,
        var listener: (fileInfo: FileInfo, action: Int)->Unit?
    ): RecyclerView.ViewHolder(itemFileBinding.root),
        View.OnCreateContextMenuListener {

        init {
            itemView.setOnClickListener {
                val info = itemFileBinding.fileInfo
                listener(info!!, 0)
            }

            itemView.setOnLongClickListener {
                val info = itemFileBinding.fileInfo
                listener(info!!, 1)
                false
            }

            itemView.setOnCreateContextMenuListener(this)
        }


        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            // Only show context menu on files
            if (itemFileBinding.fileInfo?.isDir != true) {
                menu?.add(this.adapterPosition, 200, 0, "Encrypt")
                menu?.add(this.adapterPosition, 201, 1, "Decrypt")
            }
        }

    }
}