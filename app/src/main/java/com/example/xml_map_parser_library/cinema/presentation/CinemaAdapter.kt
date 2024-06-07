package com.example.xml_map_parser_library.cinema.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.xml_map_parser_library.cinema.data.models.Cinema
import com.example.xml_map_parser_library.databinding.CinemaItemBinding

class CinemaAdapter(private val context: Context, private val cinemas: List<Cinema>) : BaseAdapter() {

    override fun getCount(): Int {
        return cinemas.size
    }

    override fun getItem(position: Int): Any {
        return cinemas[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: CinemaItemBinding
        val viewHolder: ViewHolder

        if (convertView == null) {
            binding = CinemaItemBinding.inflate(LayoutInflater.from(context), parent, false)
            viewHolder = ViewHolder(binding)
            binding.root.tag = viewHolder
        } else {
            binding = CinemaItemBinding.bind(convertView)
            viewHolder = convertView.tag as ViewHolder
        }

        val cinema = cinemas[position]

        with(viewHolder) {
            binding.picture.setImageResource(cinema.picture)
        }

        return binding.root
    }

    private class ViewHolder(val binding: CinemaItemBinding)
}