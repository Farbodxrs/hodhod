package ir.hodhod.hodhod.views.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import ir.hodhod.hodhod.R
import ir.hodhod.hodhod.databinding.FragmentRoomMapBinding
import ir.hodhod.hodhod.utils.UsernameSharedPreferences
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

    private val userPreference by lazy {
        UsernameSharedPreferences.initialWith(requireContext().applicationContext)
    }

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

    }

    private fun initialView() {
    }

    private fun subscribeViews() {
        chatViewModel.getLocationsRespond.observe(viewLifecycleOwner) {
            Log.e("Sepehr", "locations: $it")
            Log.e("latitude: $args.latitude.toDouble()", "longtitude : $args.longitude.toDouble()")

            it.keys.forEach { username ->
                if (username != userPreference.getUsername()) {
                    map.addMarker(
                        MarkerOptions().apply {
                            it[username]?.also { latLng ->
                                position(latLng)
                                icon(
                                    BitmapDescriptorFactory.fromBitmap(
                                        ResourcesCompat.getDrawable(
                                            resources,
                                            R.drawable.ic_vending_pin,
                                            requireContext().theme
                                        )!!
                                            .toBitmap()
                                    )
                                )
                                title(username)
                            }
                        })
                }
            }
        }

        chatViewModel.getLocationsError.observe(viewLifecycleOwner) {

        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        chatViewModel.getLocations(roomKey)
        map.apply {
            mapType = GoogleMap.MAP_TYPE_NORMAL
            isMyLocationEnabled = true

//          move camera to last location
            moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(args.latitude.toDouble(), args.longitude.toDouble()),
                    9f
                )
            )

            uiSettings.apply {
                isZoomGesturesEnabled = true
                isCompassEnabled = true
            }
        }
    }

}