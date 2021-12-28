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
import ir.hodhod.hodhod.viewmodels.BrokerSharedViewModel
import ir.hodhod.hodhod.viewmodels.ChatViewModel
import ir.hodhod.hodhod.viewmodels.RoomListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RoomSettingFragment : Fragment() {

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

    }

    private fun subscribeViews() {
        chatViewModel.getUsersRespond.observe(viewLifecycleOwner) {
            Log.e("Sepehr", "users: $it")
//            roomListViewModel.deleteRoom(RoomModel(roomKey))
        }

        chatViewModel.getUsersError.observe(viewLifecycleOwner) {

        }

        roomListViewModel.deleteRespond.observe(viewLifecycleOwner) {
            Log.e("Sepehr", "room deleted")
        }

        roomListViewModel.deleteError.observe(viewLifecycleOwner) {

        }
    }
}