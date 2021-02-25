package com.example.roomdemo.adepter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.innoventesdemo.R
import com.example.innoventesdemo.databinding.ShowlistItemBinding

import com.example.innoventesdemo.model.search.Search
import com.example.innoventesdemo.ui.details.SowDetailsActivity
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList


class MyRecycleview(var searchlist: List<Search>, var context: Context) :
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
                    .load(searchlist.get(position).Poster)
                    .placeholder(R.drawable.placeholder_for_missing_posters)
                    .error(R.drawable.placeholder_for_missing_posters)
                    .into(holder.binding.image)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        holder.binding.listItemLayout.setOnClickListener {
            var intent=Intent(context, SowDetailsActivity::class.java)
            intent.putExtra("id",searchlist.get(position).imdbID)
            context.startActivity(intent)
        }
        holder.binding(searchlist[position])
    }

    //for Search

    override fun getItemCount(): Int {
        return searchlist.size
    }


}

class MyViewHolder(val binding: ShowlistItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun binding(contant: Search) {
        binding.lable.text = contant.Title

    }


}