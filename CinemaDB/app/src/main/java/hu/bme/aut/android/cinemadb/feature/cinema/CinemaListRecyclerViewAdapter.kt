package hu.bme.aut.android.cinemadb.feature.cinema

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.cinemadb.databinding.FragmentCinemaBinding
import hu.bme.aut.android.cinemadb.model.cinema.Cinema

class CinemaListRecyclerViewAdapter(private val listener: OnCinemaSelectedListener) :
    RecyclerView.Adapter<CinemaListRecyclerViewAdapter.ViewHolder>() {

    private val values: MutableList<Cinema> = mutableListOf()

    interface OnCinemaSelectedListener {
        fun onCinemaSelected(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        FragmentCinemaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.displayName
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateCinemas(cinemas: List<Cinema>) {
        values.clear()
        values.addAll(cinemas)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentCinemaBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber

        init {
            binding.root.setOnClickListener { listener.onCinemaSelected(bindingAdapterPosition) }
        }
    }
}