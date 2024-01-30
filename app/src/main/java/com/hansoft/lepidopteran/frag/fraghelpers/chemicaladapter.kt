package com.hansoft.lepidopteran.frag.fraghelpers

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hansoft.lepidopteran.R
import com.hansoft.lepidopteran.database.chemicaldatabse.Chemical
import com.hansoft.lepidopteran.database.chemicaldatabse.ChemicalViewModel
import java.util.Locale

class ChemicalAdapter(fra: Context, val chemicalViewModel: ChemicalViewModel) : RecyclerView.Adapter<ChemicalAdapter.ViewHolder>() {

    private var chemical = emptyList<Chemical>()
    private var allchemical = emptyList<Chemical>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image)
        val imageViewd: ImageView = itemView.findViewById(R.id.delete)
        val textView1: TextView = itemView.findViewById(R.id.textView1)
        val textView2: TextView = itemView.findViewById(R.id.textView2)
        val textView3: TextView = itemView.findViewById(R.id.textView3)
        val textView4: TextView = itemView.findViewById(R.id.textView4)

        init {
            itemView.setOnClickListener {
                val show = textView3.visibility == View.GONE
                textView3.visibility = if (show) View.VISIBLE else View.GONE
                textView4.visibility = if (show) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chemical_adapter_view_2, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = chemical[position]
        holder.imageViewd.setOnClickListener {
            chemicalViewModel.delete(item)
        }

        when (item.icon) {
            "red" -> {
                holder.imageView.setImageResource(R.drawable.red)
            }
            "yellow" -> {
                holder.imageView.setImageResource(R.drawable.yellow)
            }
            "blue" -> {
                holder.imageView.setImageResource(R.drawable.blue)
            }
            else -> {
                holder.imageView.setImageResource(R.drawable.green)
            }
        }

        holder.textView1.text = item.name
        holder.textView2.text = "Type "+item.type
        holder.textView3.text = "Target "+item.target
        holder.textView4.text = "Rate "+item.rate
    }

    override fun getItemCount(): Int = chemical.size

    @SuppressLint("NotifyDataSetChanged")
    internal fun setPests(chemical: List<Chemical>) {
        this.chemical = chemical
        this.allchemical = ArrayList(chemical)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<Chemical>) {
        chemical = newItems
        notifyDataSetChanged()
    }

    fun getFilteredPests(text: String): List<Chemical> {
        return allchemical.filter {
            it.name.lowercase(Locale.ROOT).contains(text.lowercase(Locale.ROOT))
        }
    }
}
