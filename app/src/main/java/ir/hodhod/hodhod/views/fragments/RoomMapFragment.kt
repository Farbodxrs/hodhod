package ir.hodhod.hodhod.views.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import ir.hodhod.hodhod.R
import ir.hodhod.hodhod.databinding.FragmentRoomMapBinding
import ir.hodhod.hodhod.viewmodels.ChatViewModel


@AndroidEntryPoint
class RoomMapFragment : Fragment(), OnMapReadyCallback {

    // region of params
    private val chatViewModel by viewModels<ChatViewModel>()
    private val args by navArgs<RoomMapFragmentArgs>()
    private var _binding: FragmentRoomMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: GoogleMap

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
        _binding = FragmentRoomMapBinding.inflate(inflater, container, false)

        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync(this)

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

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.apply {
            mapType = GoogleMap.MAP_TYPE_NORMAL
            isMyLocationEnabled = true

            // move camera to last location
            moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(35.0, 51.0),
                    7f
                )
            )

            uiSettings.apply {
                isZoomGesturesEnabled = true
                isCompassEnabled = true
            }
        }
    }
}