package hu.bme.aut.android.cinemadb.feature.film

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.cinemadb.databinding.FragmentFilmBinding
import hu.bme.aut.android.cinemadb.model.film.FilmResponse.Body.Film

class FilmListRecyclerViewAdapter(private val listener: OnFilmSelectedListener) :
    RecyclerView.Adapter<FilmListRecyclerViewAdapter.ViewHolder>() {

    private val values: MutableList<Film> = mutableListOf()

    interface OnFilmSelectedListener {
        fun onFilmSelected(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        FragmentFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(values[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateFilms(films: List<Film>) {
        values.clear()
        values.addAll(films)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentFilmBinding) : RecyclerView.ViewHolder(binding.root) {
        private val idView: TextView = binding.itemNumber

        init {
            binding.root.setOnClickListener { listener.onFilmSelected(bindingAdapterPosition) }
        }

        fun bind(film: Film) {
            idView.text = film.name
        }
    }
}