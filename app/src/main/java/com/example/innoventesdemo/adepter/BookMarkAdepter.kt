package com.example.innoventesdemo.adepter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.innoventesdemo.R
import com.example.innoventesdemo.databinding.ShowlistItemBinding
import com.example.innoventesdemo.model.ShowSearchDetails

class BookMarkAdepter(var searchlist: List<ShowSearchDetails>, var context: Context, val clickLister: (ShowSearchDetails) -> Unit) :
    RecyclerView.Adapter<MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ShowlistItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.showlist_item, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {

            context.let {
                Glide.with(it)
                    .load(searchlist.get(position).poster)
                    .placeholder(R.drawable.placeholder_for_missing_posters)
                    .error(R.drawable.placeholder_for_missing_posters)
                    .into(holder.binding.image)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        holder.binding.listItemLayout.setOnClickListener {
            clickLister(searchlist.get(position))
        }
        holder.binding(searchlist[position])
    }

    //for Search

    override fun getItemCount(): Int {
        return searchlist.size
    }


}

class MyViewHolder(val binding: ShowlistItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun binding(contant: ShowSearchDetails) {
        binding.lable.text = contant.title

    }


}