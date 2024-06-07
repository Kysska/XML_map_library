package com.example.xml_map_parser_library.cinema.data

import com.example.xml_map_parser_library.cinema.data.models.Place

class PlaceDataSource {

    fun getPlace() : List<Place>{
        return listOf(
            Place(id = "1", employed = true, idCinema = "1"),
            Place(id = "10", employed = true, idCinema = "1"),
            Place(id = "15", employed = true, idCinema = "1"),
            Place(id = "11", employed = true, idCinema = "1"),
            Place(id = "2", employed = true, idCinema = "2"),
            Place(id = "3", employed = true, idCinema = "2"),
            Place(id = "9", employed = true, idCinema = "2"),
            Place(id = "19", employed = true, idCinema = "2"),
            Place(id = "5", employed = true, idCinema = "3"),
            Place(id = "6", employed = true, idCinema = "3"),
            Place(id = "17", employed = true, idCinema = "3"),
        )
    }
}