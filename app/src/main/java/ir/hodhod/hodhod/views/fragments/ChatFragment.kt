package ir.hodhod.hodhod.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import ir.hodhod.hodhod.data.models.MessageModel
import ir.hodhod.hodhod.databinding.FragmentChatBinding
import ir.hodhod.hodhod.utils.UsernameSharedPreferences
import ir.hodhod.hodhod.viewmodels.BrokerSharedViewModel
import ir.hodhod.hodhod.views.adapters.MessageAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ChatFragment : Fragment(), View.OnClickListener {

    // region of params
    private val brokerSharedViewModel by viewModels<BrokerSharedViewModel>()
    private val args by navArgs<ChatFragmentArgs>()
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val userPreference by lazy {
        UsernameSharedPreferences.initialWith(requireContext().applicationContext)
    }

    private lateinit var navController: NavController
    private lateinit var roomKey: String
    private lateinit var username: String
    private val listData = mutableListOf<MessageModel>()
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
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        initialView()
        subscribeViews()

        brokerSharedViewModel.setMessageCallback()

        username = userPreference.getUsername() ?: ""

//        listData.addAll(
//            listOf(
//                MessageModel("این پیام برای تست است.", false, 1366713508000, "sama"),
//                MessageModel("باشه.", false, 1366813508000, "sepehr"),
//                MessageModel("سلام به همگی", true, 1366913508000, "sama"),
//                MessageModel("سلام", false, 1367713508000, "sepehr")
//            )
//        )
    }

    private fun initialView() {
        binding.chatTitleTextView.text = roomKey

        binding.messageList.layoutManager = LinearLayoutManager(requireContext())
        binding.messageList.adapter = MessageAdapter(listData)

        binding.chatBackImageView.setOnClickListener(this)
        binding.btnSend.setOnClickListener(this)
    }

    private fun subscribeViews() {
        brokerSharedViewModel.publishRespond.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "published successfully", Toast.LENGTH_SHORT).show()
        }

        brokerSharedViewModel.publishError.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "publish failed", Toast.LENGTH_SHORT).show()
        }

        brokerSharedViewModel.messageDeliver.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "message delivered successfully", Toast.LENGTH_SHORT)
                .show()
        }

        brokerSharedViewModel.messageArrived.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "message arrived successfully", Toast.LENGTH_SHORT)
                .show()

            val gsonPretty = GsonBuilder().setPrettyPrinting().create()
            val message = gsonPretty.fromJson(it, MessageModel::class.java)

            if (message.username != username) {
                listData.add(
                    listData.size,
                    with(message) {
                        MessageModel(content, time, username)
                    }
                )
                binding.messageList.adapter?.notifyItemInserted(listData.size)
            }
        }

        brokerSharedViewModel.connectionLost.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "connection lost", Toast.LENGTH_SHORT).show()
        }

        brokerSharedViewModel.unsubscribeRespond.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "unsubscribed successfully", Toast.LENGTH_SHORT).show()
        }

        brokerSharedViewModel.unsubscribeError.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "unsubscribe failed", Toast.LENGTH_SHORT).show()
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
                listData.add(
                    listData.size,
                    MessageModel(msg, Date().time, username, roomKey)
                )
                binding.messageList.adapter?.notifyItemInserted(listData.size)

                val gsonPretty = GsonBuilder().setPrettyPrinting().create()
                brokerSharedViewModel.publishMessage(
                    roomKey,
                    gsonPretty.toJson(listData.last())
                )

                binding.txtMessage.setText("")
                binding.messageList.scrollToPosition(listData.size - 1)
            }
        }
    }
}