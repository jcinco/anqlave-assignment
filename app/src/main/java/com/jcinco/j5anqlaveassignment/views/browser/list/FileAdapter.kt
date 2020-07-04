package com.jcinco.j5anqlaveassignment.views.browser.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jcinco.j5anqlaveassignment.R
import com.jcinco.j5anqlaveassignment.data.model.file.FileInfo
import com.jcinco.j5anqlaveassignment.databinding.ItemFileBinding

class FileAdapter(
    private val files: ArrayList<FileInfo>,
    var itemClickListener: (fileInfo: FileInfo)->Unit?
): RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    override fun getItemCount(): Int = files.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder =
        FileViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_file,
                parent,
                false
            ),
            itemClickListener
        )

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.itemFileBinding.fileInfo = files[position]
    }


    inner class FileViewHolder(
        val itemFileBinding: ItemFileBinding,
        var listener: (fileInfo: FileInfo)->Unit?
    ): RecyclerView.ViewHolder(itemFileBinding.root) {

        init {
            itemView.setOnClickListener {
                val info = itemFileBinding.fileInfo
                listener(info!!)
            }
        }
    }
}