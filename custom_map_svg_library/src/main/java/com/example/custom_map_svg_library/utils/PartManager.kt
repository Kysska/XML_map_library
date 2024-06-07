package com.example.custom_map_svg_library.utils

import android.util.Log
import com.example.custom_map_svg_library.models.Part

internal class PartManager(private val parts: MutableList<Part> = mutableListOf()) {
    private var selectedPart : Part? = null

    fun setPart(part: List<Part>) {
        this.parts.clear()
        this.parts.addAll(part)
    }

    fun selectPart(part: Part?) {
        selectedPart = part
    }

    fun setFillColorParts(color : Int, parts : List<Part>){
        for(part in parts){
            part.color = color
        }
    }

    fun setFillColorPartsByIds(color : Int, ids : List<String>){
        for (part in parts) {
            if (part.id in ids) {
                part.color = color
            }
        }
    }


    fun getPart(): List<Part> = parts

    fun getPartById(id: String): Part? {
        val part = parts.find { it.id == id }
        if (part == null) {
            Log.e("getPartById", "Part not found for id: $id")
        }
        return part
    }

    fun getPartsByName(name: String): List<Part> {
        val result = parts.filter { it.name == name }
        if (result.isEmpty()) {
            Log.e("getPartsByName", "No parts found with name: $name")
        }
        return result
    }

    fun getPartByIndex(index: Int): Part? {
        return if (index in 0 until parts.size) {
            parts[index]
        } else {
            Log.e("getPartByIndex", "Invalid index: $index. Valid range is 0 to ${parts.size - 1}")
            null
        }
    }

    fun getSelectedPart(): Part? = selectedPart

    fun changeColorPart(part: Part, color: Int) : Boolean{
        return if (parts.contains(part)) {
            part.color = color
            true
        } else {
            Log.e("changeColorPart", "Part is not found in the list")
            false
        }
    }

    fun changeColorPart(partId: String, color: Int) : Boolean{
        val part = parts.find { it.id == partId }
        return if (part != null) {
            part.color = color
            true
        } else {
            Log.e("changeColorPart", "Part with id $partId not found")
            false
        }
    }

    fun handleTouch(x: Float, y: Float): Part? {
        for (part in parts) {
            if (part.region.contains(x.toInt(), y.toInt())) {
                return part
            }
        }
        return null
    }
}