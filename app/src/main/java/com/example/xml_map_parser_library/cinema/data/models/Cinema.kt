package com.example.xml_map_parser_library.cinema.data.models

import java.io.Serializable

data class Cinema(
    val id : String,
    val name : String,
    val picture : Int,
    val desc : String,
    val price : Int,
    val time : String
) : Serializable
