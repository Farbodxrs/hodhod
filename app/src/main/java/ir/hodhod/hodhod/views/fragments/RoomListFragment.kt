package ir.hodhod.hodhod.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.hodhod.hodhod.R
import ir.hodhod.hodhod.data.models.RoomModel
import ir.hodhod.hodhod.databinding.FragmentRoomListBinding
import ir.hodhod.hodhod.utils.UsernameSharedPreferences
import ir.hodhod.hodhod.viewmodels.BrokerSharedViewModel
import ir.hodhod.hodhod.viewmodels.RoomListViewModel
import ir.hodhod.hodhod.views.adapters.RoomAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*
import kotlin.random.Random

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RoomListFragment : Fragment(), JoinRoomPopupFragment.JoinClickListener, View.OnClickListener,
    RoomAdapter.ItemClickListener {

    // region of params
    private val brokerSharedViewModel by viewModels<BrokerSharedViewModel>()
    private val roomListViewModel by viewModels<RoomListViewModel>()
    private var _binding: FragmentRoomListBinding? = null
    private val binding get() = _binding!!

    private val userPreference by lazy {
        UsernameSharedPreferences.initialWith(requireContext().applicationContext)
    }

    private val roomList = mutableListOf<RoomModel>()

    // END of region of params

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoomListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialView()
        subscribeViews()

        setUsername()

        roomListViewModel.getAllRooms()
    }

    private fun initialView() {
        binding.tvRoomListNoRoom.visibility = View.VISIBLE
        binding.roomListRV.layoutManager = LinearLayoutManager(requireContext())
        binding.roomListRV.adapter = RoomAdapter(roomList, this)

        binding.btnAddRoom.setOnClickListener(this)
    }

    private fun subscribeViews() {
        brokerSharedViewModel.subscribeRespond.observe(viewLifecycleOwner) {
//            Toast.makeText(requireContext(), "subscribed successfully", Toast.LENGTH_SHORT).show()
        }

        brokerSharedViewModel.subscribeError.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), R.string.problem_occurred, Toast.LENGTH_SHORT).show()
        }

        roomListViewModel.getRoomsRespond.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                roomList.clear()
                roomList.addAll(it)
                binding.tvRoomListNoRoom.visibility = View.GONE
                binding.roomListRV.adapter?.notifyItemRangeChanged(0, roomList.size)
            } else binding.tvRoomListNoRoom.visibility = View.VISIBLE
        }

        roomListViewModel.getRoomsError.observe(viewLifecycleOwner) {
            binding.tvRoomListNoRoom.visibility = View.VISIBLE
        }
    }

    private fun setUsername() {
        userPreference.getUsername()?.also {
            binding.usernameTextView.text = it
        } ?: apply {
            val name = "user-${Random.nextInt(1000, 10000)}"
            userPreference.saveUsername(name)

            binding.usernameTextView.text = name
        }
    }

    override fun onJoinClickListener(key: String) {
        brokerSharedViewModel.subscribeToTopic(key)

        val room = RoomModel(key, Date().time)
        roomList.add(0, room)
        roomListViewModel.insertRoom(room)
        binding.tvRoomListNoRoom.visibility = View.GONE
        binding.roomListRV.adapter?.notifyItemInserted(0)

        findNavController().navigate(
            RoomListFragmentDirections.actionRoomListFragmentToChatFragment(key)
        )
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnAddRoom -> JoinRoomPopupFragment.getInstance(this)
                .show(parentFragmentManager, JoinRoomPopupFragment.JOIN_POPUP_TAG)
        }
    }

    override fun onItemClickListener(key: String) {
        brokerSharedViewModel.subscribeToTopic(key)

        findNavController().navigate(
            RoomListFragmentDirections.actionRoomListFragmentToChatFragment(key)
        )
    }
}