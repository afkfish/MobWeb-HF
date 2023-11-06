package hu.bme.aut.android.cinemadb.feature.film

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.cinemadb.databinding.FragmentFilmBinding
import hu.bme.aut.android.cinemadb.model.film.FilmResponse

class MyFilmListRecyclerViewAdapter(
    response: FilmResponse,
    private val listener: OnFilmSelectedListener
) : RecyclerView.Adapter<MyFilmListRecyclerViewAdapter.ViewHolder>() {

    private val values = response.body.films

    interface OnFilmSelectedListener {
        fun onFilmSelected()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentFilmBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.name
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentFilmBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber

        init {
            binding.root.setOnClickListener { listener.onFilmSelected() }
        }
    }
}