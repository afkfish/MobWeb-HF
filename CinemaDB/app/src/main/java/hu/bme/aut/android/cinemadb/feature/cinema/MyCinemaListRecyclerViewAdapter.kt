package hu.bme.aut.android.cinemadb.feature.cinema

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.cinemadb.databinding.FragmentCinemaBinding
import hu.bme.aut.android.cinemadb.model.cinema.CinemaResponse

class MyCinemaListRecyclerViewAdapter(
    response: CinemaResponse,
    private val listener: OnCinemaSelectedListener
) : RecyclerView.Adapter<MyCinemaListRecyclerViewAdapter.ViewHolder>() {

    private val values = response.body.cinemas

    interface OnCinemaSelectedListener {
        fun onCinemaSelected()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentCinemaBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.displayName
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentCinemaBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber

        init {
            binding.root.setOnClickListener { listener.onCinemaSelected() }
        }
    }
}