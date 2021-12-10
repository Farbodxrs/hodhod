package ir.hodhod.hodhod.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.hodhod.hodhod.databinding.FragmentRoomListBinding
import ir.hodhod.hodhod.models.RoomModel
import ir.hodhod.hodhod.viewmodels.SharedViewModel
import ir.hodhod.hodhod.views.adapters.RoomAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RoomListFragment : Fragment(), JoinRoomPopupFragment.JoinClickListener, View.OnClickListener {

    // region of params
    private val sharedViewModel by viewModels<SharedViewModel>()
    private var _binding: FragmentRoomListBinding? = null
    private val binding get() = _binding!!

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

        sharedViewModel.connectToServer()
    }

    private fun initialView() {
        binding.roomListRV.layoutManager = LinearLayoutManager(requireContext())
        binding.roomListRV.adapter = RoomAdapter(roomList ?: mutableListOf())

        binding.btnAddRoom.setOnClickListener(this)
    }

    override fun onJoinClickListener(key: String) {
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

    companion object {

        // TODO: we should get room list from database, it's a temporary solution!
        private var roomList: MutableList<RoomModel>? = null
    }
}