package com.example.xml_map_parser_library.russiamap.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.custom_map_svg_library.models.Part
import com.example.custom_map_svg_library.utils.OnPartClickListener
import com.example.xml_map_parser_library.R
import com.example.xml_map_parser_library.databinding.FragmentRussiaMapBinding

class RussiaMapFragment : Fragment() {

    private lateinit var binding : FragmentRussiaMapBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRussiaMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val xmlResourceId = resources.getIdentifier("russia", "raw", requireActivity().packageName)
        val mySelectedFillColor = ContextCompat.getColor(requireContext(), R.color.region_selected)
        val mySelectedStrokeColor = ContextCompat.getColor(requireContext(), R.color.white)
        binding.apply {
            russiaMap.apply {
                showScheme(xmlResourceId)
                selectedFillColor = mySelectedFillColor
                strokeWidth = 0.7f
                selectedStrokeColor = mySelectedStrokeColor

                setOnPartClickListener(object : OnPartClickListener{
                    override fun onPartClick(part: Part) {
                        clickToPart(part)
                        zoomToPart(part)
                        val bottomSheetFragment = RegionDetailsBottomFragment.newInstance(part.name)
                        bottomSheetFragment.show(childFragmentManager, RegionDetailsBottomFragment.TAG)
                    }
                })

                buttonPlus.setOnClickListener {
                    zoomIn()
                }
                buttonMinus.setOnClickListener {
                    zoomOut()
                }
                buttonEquals.setOnClickListener {
                    resetZoom()
                }
            }

        }
    }

    companion object {
        const val TAG = "RussiaMapFragment"
        fun newInstance() : RussiaMapFragment{
            return RussiaMapFragment()
        }
    }
}