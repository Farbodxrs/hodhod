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
import ir.hodhod.hodhod.data.models.RoomModel
import ir.hodhod.hodhod.databinding.FragmentRoomListBinding
import ir.hodhod.hodhod.utils.UsernameSharedPreferences
import ir.hodhod.hodhod.viewmodels.BrokerSharedViewModel
import ir.hodhod.hodhod.views.adapters.RoomAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.random.Random

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RoomListFragment : Fragment(), JoinRoomPopupFragment.JoinClickListener, View.OnClickListener,
    RoomAdapter.ItemClickListener {

    // region of params
    private val brokerSharedViewModel by viewModels<BrokerSharedViewModel>()
    private var _binding: FragmentRoomListBinding? = null
    private val binding get() = _binding!!

    private val userPreference by lazy {
        UsernameSharedPreferences.initialWith(requireContext().applicationContext)
    }

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

        if (roomList == null) roomList = mutableListOf()

        initialView()
        subscribeViews()

        setUsername()
    }

    private fun initialView() {
        binding.roomListRV.layoutManager = LinearLayoutManager(requireContext())
        binding.roomListRV.adapter = RoomAdapter(roomList ?: mutableListOf(), this)

        binding.btnAddRoom.setOnClickListener(this)
    }

    private fun subscribeViews() {
        brokerSharedViewModel.subscribeRespond.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "subscribed successfully", Toast.LENGTH_SHORT).show()
        }

        brokerSharedViewModel.subscribeError.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "subscribe failed", Toast.LENGTH_SHORT).show()
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

        roomList?.add(RoomModel(key))

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

    companion object {

        // TODO: we should get room list from database, it's a temporary solution!
        private var roomList: MutableList<RoomModel>? = null
    }
}