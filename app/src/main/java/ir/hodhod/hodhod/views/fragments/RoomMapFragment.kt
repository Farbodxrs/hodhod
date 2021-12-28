package ir.hodhod.hodhod.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ir.hodhod.hodhod.databinding.FragmentRoomSettingBinding
import ir.hodhod.hodhod.viewmodels.ChatViewModel


@AndroidEntryPoint
class RoomMapFragment : Fragment() {

    // region of params
    private val chatViewModel by viewModels<ChatViewModel>()
    private val args by navArgs<RoomSettingFragmentArgs>()
    private var _binding: FragmentRoomSettingBinding? = null
    private val binding get() = _binding!!

    private lateinit var roomKey: String

    // END of region of params

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        roomKey = args.roomKey
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoomSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialView()
        subscribeViews()

        chatViewModel.getLocations(roomKey)
    }

    private fun initialView() {

    }

    private fun subscribeViews() {
        chatViewModel.getLocationsRespond.observe(viewLifecycleOwner) {
            Log.e("Sepehr", "locations: $it")
        }

        chatViewModel.getLocationsError.observe(viewLifecycleOwner) {

        }
    }
}