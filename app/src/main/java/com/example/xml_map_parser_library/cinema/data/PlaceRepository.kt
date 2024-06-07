package com.example.xml_map_parser_library.cinema.data

import com.example.xml_map_parser_library.cinema.data.models.Place

class PlaceRepository(private val placeDataSource: PlaceDataSource) {
    fun getPlace() : List<Place>{
        return placeDataSource.getPlace()
    }
}