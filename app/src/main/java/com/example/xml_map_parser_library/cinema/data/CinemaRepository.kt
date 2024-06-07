package com.example.xml_map_parser_library.cinema.data

import com.example.xml_map_parser_library.cinema.data.models.Cinema

class CinemaRepository(private val cinemaDataSource: CinemaDataSource) {
    fun getCinemas() : List<Cinema>{
        return cinemaDataSource.getCinemas()
    }
}