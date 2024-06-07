package com.example.xml_map_parser_library.russiamap.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.xml_map_parser_library.R
import com.example.xml_map_parser_library.databinding.FragmentRegionDetailsBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RegionDetailsBottomFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentRegionDetailsBottomBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegionDetailsBottomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val partName = arguments?.getString("partName")
        binding.partName.text = partName
    }

    companion object {
        const val TAG = "RegionDetailsBottomFragment"

        fun newInstance(partName: String): RegionDetailsBottomFragment {
            val args = Bundle()
            args.putString("partName", partName)
            val fragment = RegionDetailsBottomFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
