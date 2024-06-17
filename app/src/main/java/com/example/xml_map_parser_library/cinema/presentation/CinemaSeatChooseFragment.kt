package com.example.xml_map_parser_library.cinema.presentation

import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.custom_map_svg_library.models.Marker
import com.example.custom_map_svg_library.models.Part
import com.example.custom_map_svg_library.utils.OnPartClickListener
import com.example.xml_map_parser_library.R
import com.example.xml_map_parser_library.cinema.data.PlaceDataSource
import com.example.xml_map_parser_library.cinema.data.PlaceRepository
import com.example.xml_map_parser_library.cinema.data.models.Cinema
import com.example.xml_map_parser_library.databinding.FragmentCinemaSeatChooseBinding

class CinemaSeatChooseFragment : Fragment() {

    private lateinit var binding: FragmentCinemaSeatChooseBinding
    private lateinit var dataRepository: PlaceRepository
    private var totalSum = 0
    private val markers = mutableMapOf<Part, Marker>()
    private val selectedPart = mutableListOf<Part>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val placeDataSource = PlaceDataSource()
        dataRepository = PlaceRepository(placeDataSource)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCinemaSeatChooseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        val cinema = getCinema()
        val places = dataRepository.getPlace().filter { it.idCinema == cinema.id }
        val placeId = places.map { it.id }
        val xmlResourceId = resources.getIdentifier("cinema", "raw", requireActivity().packageName)

        val mySelectedFillColor = ContextCompat.getColor(requireContext(), R.color.place_selected)
        val reservedFillColor = ContextCompat.getColor(requireContext(), R.color.place_reserved)

        binding.apply {
            name.text = cinema.name
            time.text = cinema.time
            textDesc.text = cinema.desc
            setupCleanTextButton()
            schemeView.apply {
                showScheme(xmlResourceId)
                selectedFillColor = mySelectedFillColor
                isMultiSelectEnabled = true
                handlePartClick(cinema)
                fillPartsWithColorByIds(reservedFillColor, placeId)
            }
        }
    }

    private fun handlePartClick(cinema : Cinema) {
        binding.schemeView.apply {
            setOnPartClickListener(object : OnPartClickListener {
                override fun onPartClick(part: Part) {
                    if(part in selectedPart){
                        deselectPart(part)
                        selectedPart.remove(part)

                        val markerToRemove = markers[part]
                        markerToRemove?.let {
                            markers.remove(part)
                            removeMarker(it)
                        }
                    }
                    else{
                        clickToPart(part)
                        selectedPart.add(part)

                        val marker = addMarker(part, 30f, drawable = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_done_24))
                        markers[part] = marker

                        settingPartClick(part, cinema)
                    }

                }
            })
        }

    }

    private fun setupCleanTextButton() {
        binding.apply {
            cleanTextButton.setOnClickListener {
                schemeView.deselectAllParts()
                schemeView.removeAllMarkers()
                listPlaces.removeAllViews()
                totalSum = 0
                price.text = "${totalSum}руб."
            }
        }
    }

    private fun settingPartClick(part: Part, cinema: Cinema) {

        val textPlaceName = TextView(requireContext()).apply {
            text = "Место ${part.id} - ${cinema.price}руб."
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        totalSum += cinema.price
        binding.listPlaces.addView(textPlaceName)
        binding.price.text = "${totalSum}руб."
    }

    private fun getCinema(): Cinema {
        return requireArguments().getSerializable(CINEMA) as Cinema
    }

    companion object {
        private const val CINEMA = "cinema"
        const val TAG = "CinemaSeatChooseFragment"

        fun newInstance(cinema: Cinema): CinemaSeatChooseFragment {
            val arguments = Bundle().apply {
                putSerializable(CINEMA, cinema)
            }
            val fragment = CinemaSeatChooseFragment()
            fragment.arguments = arguments
            return fragment
        }
    }
}
