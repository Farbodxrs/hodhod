package ir.hodhod.hodhod.views.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import ir.hodhod.hodhod.data.models.MessageModel
import ir.hodhod.hodhod.databinding.FragmentChatBinding
import ir.hodhod.hodhod.utils.UsernameSharedPreferences
import ir.hodhod.hodhod.viewmodels.BrokerSharedViewModel
import ir.hodhod.hodhod.viewmodels.ChatViewModel
import ir.hodhod.hodhod.views.adapters.MessageAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ChatFragment : Fragment(), View.OnClickListener {

    // region of params
    private val brokerSharedViewModel by viewModels<BrokerSharedViewModel>()
    private val chatViewModel by viewModels<ChatViewModel>()
    private val args by navArgs<ChatFragmentArgs>()
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val userPreference by lazy {
        UsernameSharedPreferences.initialWith(requireContext().applicationContext)
    }

    private lateinit var navController: NavController
    private lateinit var roomKey: String
    private lateinit var username: String
    private val gsonPretty = GsonBuilder().setPrettyPrinting().create()
    private var chatListData = mutableListOf<MessageModel>()
    private lateinit var messageAdapter: MessageAdapter

    private var currentLocation: LatLng? = null
    private lateinit var locationRequest: LocationRequest
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            currentLocation = locationResult.lastLocation.run { LatLng(latitude, longitude) }
        }
    }
    private lateinit var locationManager: LocationManager

    // END of region of params

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        roomKey = args.roomKey

        locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationRequest = LocationRequest.create().apply {
            interval = 5000L
            fastestInterval = 5000L
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }

        if (isLocationGranted()) {
            // getting user location
            LocationServices.getFusedLocationProviderClient(requireContext())
                .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        initialView()
        subscribeViews()

        chatViewModel.getAllMessages(roomKey)
        brokerSharedViewModel.setMessageCallback()

        username = userPreference.getUsername() ?: ""

        if (!isLocationGranted())
            navController.navigate(ChatFragmentDirections.actionChatFragmentToLocationBottomSheetFragment())
    }

    private fun initialView() {
        binding.chatTitleTextView.text = roomKey

        binding.tvChatNoMessage.visibility = View.VISIBLE
        binding.messageList.layoutManager = LinearLayoutManager(requireContext())
        messageAdapter = MessageAdapter(chatListData).apply {
            setHasStableIds(true)
        }
        binding.messageList.adapter = messageAdapter

        binding.chatBackImageView.setOnClickListener(this)
        binding.btnSend.setOnClickListener(this)
        binding.chatTitleTextView.setOnClickListener(this)
        binding.chatMapImageView.setOnClickListener(this)
    }

    private fun subscribeViews() {
        brokerSharedViewModel.publishRespond.observe(viewLifecycleOwner) {
//            Toast.makeText(requireContext(), "published successfully", Toast.LENGTH_SHORT).show()
        }

        brokerSharedViewModel.publishError.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "publish failed", Toast.LENGTH_SHORT).show()
        }

        brokerSharedViewModel.messageDeliver.observe(viewLifecycleOwner) {
//            Toast.makeText(requireContext(), "message delivered successfully", Toast.LENGTH_SHORT)
//                .show()
        }

        brokerSharedViewModel.messageArrived.observe(viewLifecycleOwner) {
//            Toast.makeText(requireContext(), "message arrived successfully", Toast.LENGTH_SHORT)
//                .show()

            val message = gsonPretty.fromJson(it, MessageModel::class.java)
            val newList = mutableListOf<MessageModel>()
            newList.addAll(chatListData)

            if (message.username != username) {
                newList.add(
                    with(message) {
                        MessageModel(
                            content,
                            Date().time, // because we want to display local time
                            username,
                            roomKey,
                            location
                        )
                    }.also { messageModel -> chatViewModel.insertMessage(messageModel) }
                )
                Log.e("Sepehr", "hi1 ${newList.size} | ${newList.last()}")
                binding.tvChatNoMessage.visibility = View.GONE
                messageAdapter.updateData(newList)
                binding.messageList.scrollToPosition(newList.size.minus(1))
                chatListData.clear()
                chatListData.addAll(newList)
            }
        }

        brokerSharedViewModel.connectionLost.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "connection lost", Toast.LENGTH_SHORT).show()
        }

        brokerSharedViewModel.unsubscribeRespond.observe(viewLifecycleOwner) {
//            Toast.makeText(requireContext(), "unsubscribed successfully", Toast.LENGTH_SHORT).show()
        }

        brokerSharedViewModel.unsubscribeError.observe(viewLifecycleOwner) {
//            Toast.makeText(requireContext(), "unsubscribe failed", Toast.LENGTH_SHORT).show()
        }

        chatViewModel.getMessagesRespond.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                chatListData.clear()
                chatListData.addAll(it)
                binding.tvChatNoMessage.visibility = View.GONE
                binding.messageList.adapter?.notifyItemRangeChanged(0, chatListData.size)
                binding.messageList.scrollToPosition(chatListData.size.minus(1))
            } else binding.tvChatNoMessage.visibility = View.VISIBLE
        }

        chatViewModel.getMessagesError.observe(viewLifecycleOwner) {
            binding.tvChatNoMessage.visibility = View.VISIBLE
        }
    }

    private fun isLocationGranted() =
        ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()

        if (isLocationGranted()) {
            // getting user location
            LocationServices.getFusedLocationProviderClient(requireContext())
                .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.chatBackImageView -> {
                brokerSharedViewModel.unsubscribeFromTopic(roomKey)

                navController.navigateUp()
            }

            binding.btnSend -> {
                val msg = binding.txtMessage.text.toString()
                val messageModel =
                    MessageModel(msg, Date().time, username, roomKey, currentLocation)
                chatViewModel.insertMessage(messageModel)
                val newList = mutableListOf<MessageModel>()
                newList.addAll(chatListData)
                newList.add(messageModel)
                Log.e("Sepehr", "hi2 ${newList.size} | ${newList.last()}")
                binding.tvChatNoMessage.visibility = View.GONE
                messageAdapter.updateData(newList)

                brokerSharedViewModel.publishMessage(
                    roomKey,
                    gsonPretty.toJson(messageModel)
                )

                binding.txtMessage.setText("")
                binding.messageList.scrollToPosition(newList.size.minus(1))
                chatListData.clear()
                chatListData.addAll(newList)
            }

            binding.chatTitleTextView -> navController.navigate(
                ChatFragmentDirections.actionChatFragmentToRoomSettingFragment(roomKey)
            )

            binding.chatMapImageView ->
                if (isLocationGranted())
                    currentLocation?.let {
                        navController.navigate(
                            ChatFragmentDirections.actionChatFragmentToRoomMapFragment(
                                roomKey,
                                it.latitude.toFloat(),
                                it.longitude.toFloat()
                            )
                        )
                    }
                else navController.navigate(
                    ChatFragmentDirections.actionChatFragmentToLocationBottomSheetFragment()
                )
        }
    }
}