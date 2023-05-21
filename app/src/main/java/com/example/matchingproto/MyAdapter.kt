package com.example.matchingproto

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.matchingproto.databinding.ActivityMainBinding
import com.example.matchingproto.databinding.ItemMainBinding


class MyAdapter(val datas: MutableList<ListData>,val main: MainActivity):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(ItemMainBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding
        binding.itemData.text=datas[position].title
        binding.itemBody.text=datas[position].body
        binding.itemRoot.setOnClickListener{
            main.moveMap(datas[position].latitude,datas[position].longitude,datas[position].title)

        }
        binding.showBody.setOnClickListener{
            binding.itemBody.visibility= View.VISIBLE
            binding.participateBtn.visibility=View.VISIBLE
        }

        binding.participateBtn.setOnClickListener{
            Log.d("log",datas[position].partyID)
            Log.d("log","내용내용")
            main.setParticipate(datas[position].partyID)

        }



    }

}