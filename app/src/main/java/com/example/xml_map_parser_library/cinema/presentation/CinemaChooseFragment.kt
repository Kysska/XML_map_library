import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.xml_map_parser_library.R
import com.example.xml_map_parser_library.cinema.data.CinemaDataSource
import com.example.xml_map_parser_library.cinema.data.CinemaRepository
import com.example.xml_map_parser_library.cinema.data.PlaceDataSource
import com.example.xml_map_parser_library.cinema.presentation.CinemaAdapter
import com.example.xml_map_parser_library.cinema.presentation.CinemaSeatChooseFragment
import com.example.xml_map_parser_library.databinding.FragmentCinemaChooseBinding

class CinemaChooseFragment : Fragment() {

    private lateinit var binding: FragmentCinemaChooseBinding
    private lateinit var dataRepository: CinemaRepository
    private lateinit var adapter: CinemaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cinemaDataSource = CinemaDataSource()
        dataRepository = CinemaRepository(cinemaDataSource)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCinemaChooseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cinemas = dataRepository.getCinemas()
        adapter = CinemaAdapter(requireContext(), cinemas)
        binding.lv.adapter = adapter

        binding.lv.setOnItemClickListener { adapterView, view, i, l ->
            val selectedCinema = cinemas[i]

            val fragment = CinemaSeatChooseFragment.newInstance(selectedCinema)
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, fragment)
                .addToBackStack(CinemaSeatChooseFragment.TAG)
                .commit()
        }
    }

    companion object{
        const val TAG = "CinemaChooseFragment"
        fun newInstance() : CinemaChooseFragment{
            return CinemaChooseFragment()
        }
    }
}
