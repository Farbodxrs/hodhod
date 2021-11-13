package ir.hodhod.hodhod.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ir.hodhod.hodhod.databinding.FragmentRoomListBinding
import ir.hodhod.hodhod.models.RoomModel
import ir.hodhod.hodhod.views.adapters.RoomAdapter

class RoomListFragment : Fragment(), JoinRoomPopupFragment.JoinClickListener, View.OnClickListener {

    // region of params
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

        initialView()
    }

    private fun initialView() {
        binding.roomListRV.layoutManager = LinearLayoutManager(requireContext())
        binding.roomListRV.adapter = RoomAdapter(
            listOf(
                RoomModel("Room 01"),
                RoomModel("Best room in the world")
            )
        )

        binding.textViewRoom.setOnClickListener(this)
    }

    override fun onJoinClickListener(key: String) {
        findNavController().navigate(
            RoomListFragmentDirections.actionRoomListFragmentToChatFragment(key)
        )
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.textViewRoom -> JoinRoomPopupFragment.getInstance(this)
                .show(parentFragmentManager, JoinRoomPopupFragment.JOIN_POPUP_TAG)
        }
    }
}