package com.hansoft.lepidopteran.frag.fraghelpers

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hansoft.lepidopteran.R
import com.hansoft.lepidopteran.database.pestsdatabase.Pest
import com.hansoft.lepidopteran.database.pestsdatabase.PestViewModel
import java.util.Locale

class PestAdapter(fra: Context, val pestViewModel: PestViewModel) : RecyclerView.Adapter<PestAdapter.ViewHolder>() {

    private val fraa = fra;
    private var pests = emptyList<Pest>()
    private var allPests = emptyList<Pest>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image)
        val imageViewd: ImageView = itemView.findViewById(R.id.delete)
        val textView1: TextView = itemView.findViewById(R.id.textView1)
        val textView0: TextView = itemView.findViewById(R.id.textView0)
        val textView2: TextView = itemView.findViewById(R.id.textView2)
        val textView3: TextView = itemView.findViewById(R.id.textView3)

        init {
            itemView.setOnClickListener {
                val show = textView3.visibility == View.GONE
                textView3.visibility = if (show) View.VISIBLE else View.GONE
                textView2.visibility = if (show) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expandable, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = pests[position]
        holder.imageViewd.setOnClickListener {
            pestViewModel.delete(item)
        }
        if (item.image.startsWith("a")){
            if (item.image.equals("a01")){
                holder.imageView.setImageResource(R.drawable.a01)
            } else if(item.image.equals("a02")){
                holder.imageView.setImageResource(R.drawable.a02)
            } else if(item.image.equals("a03")){
                holder.imageView.setImageResource(R.drawable.a03)
            } else if(item.image.equals("a04")){
                holder.imageView.setImageResource(R.drawable.a04)
            } else if(item.image.equals("a05")){
                holder.imageView.setImageResource(R.drawable.a05)
            } else if(item.image.equals("a06")){
                holder.imageView.setImageResource(R.drawable.a06)
            } else if(item.image.equals("a07")){
                holder.imageView.setImageResource(R.drawable.a07)
            } else if(item.image.equals("a08")){
                holder.imageView.setImageResource(R.drawable.a08)
            } else if(item.image.equals("a09")){
                holder.imageView.setImageResource(R.drawable.a09)
            } else {
                holder.imageView.setImageResource(R.drawable.a10)
            }
        } else {
            Glide.with(fraa)
                .load(item.image)
                .into(holder.imageView)
        }
        holder.textView1.text = item.commonName
        holder.textView2.text = "Scientific Name "+item.scientificName
        holder.textView3.text = item.description
        holder.textView0.text = "Impact Level "+item.impactLevel
    }

    override fun getItemCount(): Int = pests.size

    @SuppressLint("NotifyDataSetChanged")
    internal fun setPests(pests: List<Pest>) {
        this.pests = pests
        this.allPests = ArrayList(pests)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<Pest>) {
        pests = newItems
        notifyDataSetChanged()
    }

    fun getFilteredPests(text: String): List<Pest> {
        return allPests.filter {
            it.commonName.lowercase(Locale.ROOT).contains(text.lowercase(Locale.ROOT))
        }
    }
}
