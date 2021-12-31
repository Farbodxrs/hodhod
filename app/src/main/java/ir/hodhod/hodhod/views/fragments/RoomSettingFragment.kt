package ir.hodhod.hodhod.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.hodhod.hodhod.data.models.RoomModel
import ir.hodhod.hodhod.databinding.FragmentRoomSettingBinding
import ir.hodhod.hodhod.viewmodels.BrokerSharedViewModel
import ir.hodhod.hodhod.viewmodels.ChatViewModel
import ir.hodhod.hodhod.viewmodels.RoomListViewModel
import ir.hodhod.hodhod.views.adapters.RoomAdapter
import ir.hodhod.hodhod.views.adapters.UserAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RoomSettingFragment : Fragment(), View.OnClickListener {

    // region of params
    private val brokerSharedViewModel by viewModels<BrokerSharedViewModel>()
    private val chatViewModel by viewModels<ChatViewModel>()
    private val roomListViewModel by viewModels<RoomListViewModel>()
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

        chatViewModel.getUsers(roomKey)
    }

    private fun initialView() {
        binding.btnLeave.setOnClickListener(this)
        binding.chatBackImageView.setOnClickListener(this)

    }

    private fun subscribeViews() {
        chatViewModel.getUsersRespond.observe(viewLifecycleOwner) {
            Log.e("Sepehr", "users: $it")
//            roomListViewModel.deleteRoom(RoomModel(roomKey))
            if (it.isEmpty()) {
                binding.tvChatNoMessage.visibility = View.VISIBLE
            } else {
                binding.rvUser.layoutManager = LinearLayoutManager(requireContext())
                binding.rvUser.adapter = UserAdapter(it)
                binding.tvChatNoMessage.visibility = View.GONE
            }

        }

        chatViewModel.getUsersError.observe(viewLifecycleOwner) {

        }

        roomListViewModel.deleteRespond.observe(viewLifecycleOwner) {
            Log.e("Sepehr", "room deleted")
            findNavController().navigate(RoomSettingFragmentDirections.actionRoomSettingFragmentToRoomListFragment())
        }

        roomListViewModel.deleteError.observe(viewLifecycleOwner) {

        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnLeave -> {
                roomListViewModel.deleteRoom(RoomModel(roomKey))
            }
            binding.chatBackImageView -> {
                findNavController().navigateUp()
            }
        }
    }
}