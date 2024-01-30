package com.hansoft.lepidopteran.frag.fraghelpers

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hansoft.lepidopteran.R
import com.hansoft.lepidopteran.database.trackerdatabase.Tracker
import com.hansoft.lepidopteran.database.trackerdatabase.TrackerViewModel
import java.util.Locale

class TrackerAdapter(val trackerViewModel: TrackerViewModel) : RecyclerView.Adapter<TrackerAdapter.ViewHolder>() {

    private var Tracker = emptyList<Tracker>()
    private var allTracker = emptyList<Tracker>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewd: ImageView = itemView.findViewById(R.id.delete)
        val textView1: TextView = itemView.findViewById(R.id.textView1)
        val textView2: TextView = itemView.findViewById(R.id.textView2)
        val textView3: TextView = itemView.findViewById(R.id.textView3)
        val textView4: TextView = itemView.findViewById(R.id.textView4)
        val textView5: TextView = itemView.findViewById(R.id.textView5)

        init {
            itemView.setOnClickListener {
                val show = textView3.visibility == View.GONE
                textView3.visibility = if (show) View.VISIBLE else View.GONE
                textView4.visibility = if (show) View.VISIBLE else View.GONE
                textView5.visibility = if (show) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chemical_adapter_view, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = Tracker[position]
        holder.imageViewd.setOnClickListener {
            trackerViewModel.delete(item)
        }
        holder.textView1.text = item.chemical
        holder.textView2.text = item.date
        holder.textView3.text = "Target "+item.target
        holder.textView4.text = "Area "+item.area
        holder.textView5.text = "Effectiveness "+item.effectiveness
    }

    override fun getItemCount(): Int = Tracker.size

    @SuppressLint("NotifyDataSetChanged")
    internal fun setTracker(Trackers: List<Tracker>) {
        this.Tracker = Trackers
        this.allTracker = ArrayList(Trackers)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<Tracker>) {
        Tracker = newItems
        notifyDataSetChanged()
    }

    fun getFilteredPests(text: String): List<Tracker> {
        return allTracker.filter {
            it.chemical.lowercase(Locale.ROOT).contains(text.lowercase(Locale.ROOT))
        }
    }

    fun getFilteredPests2(text: String): List<Tracker> {
        return allTracker.filter {
            it.date.lowercase(Locale.ROOT).contains(text.lowercase(Locale.ROOT))
        }
    }
}
