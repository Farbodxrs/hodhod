package ir.hodhod.hodhod.views.fragments

import android.os.Bundle
import android.util.Log
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
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.AndroidEntryPoint
import ir.hodhod.hodhod.databinding.FragmentChatBinding
import ir.hodhod.hodhod.models.MessageModel
import ir.hodhod.hodhod.viewmodels.SharedViewModel
import ir.hodhod.hodhod.views.adapters.MessageAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.random.Random
import com.google.gson.Gson
import com.google.gson.GsonBuilder


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ChatFragment : Fragment(), View.OnClickListener {

    // region of params
    private val listData =  mutableListOf<MessageModel>()
    private val sharedViewModel by viewModels<SharedViewModel>()
    private val args by navArgs<ChatFragmentArgs>()
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
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
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        initialView()
        subscribeViews()

        sharedViewModel.setMessageCallback()
    }

    private fun initialView() {
        binding.chatTitleTextView.text = roomKey

        binding.messageList.layoutManager = LinearLayoutManager(requireContext())
        binding.messageList.adapter = MessageAdapter( listData
            /*listOf(
                MessageModel("این پیام برای تست است.", false, 1366713508000, "sama"),
                MessageModel("باشه.", false, 1366713508000, "sepehr"),
                MessageModel("سلام به همگی", true,1366713508000, "sama"),
                MessageModel("سلام", false,1366713508000, "sepehr")
            )*/
        )
        binding.chatBackImageView.setOnClickListener(this)
        binding.chatTitleTextView.setOnClickListener(this)
        binding.btnSend.setOnClickListener(this)
    }

    private fun subscribeViews() {
        sharedViewModel.publishRespond.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "published successfully", Toast.LENGTH_SHORT).show()
        }

        sharedViewModel.publishError.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "publish failed", Toast.LENGTH_SHORT).show()
        }

        sharedViewModel.messageDeliver.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "message delivered successfully", Toast.LENGTH_SHORT)
                .show()
        }

        sharedViewModel.messageArrived.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "message arrived successfully", Toast.LENGTH_SHORT)
                .show()
        }

        sharedViewModel.connectionLost.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "connection lost", Toast.LENGTH_SHORT).show()
        }

        sharedViewModel.unsubscribeRespond.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "unsubscribed successfully", Toast.LENGTH_SHORT).show()
        }

        sharedViewModel.unsubscribeError.observe(viewLifecycleOwner) {
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
                sharedViewModel.unsubscribeFromTopic(roomKey)

                navController.navigateUp()
            }

            binding.chatTitleTextView -> {
                val rand = Random.nextInt(1000)
                Log.d("MQTTBrokerRepository", "$rand")

            }

            binding.btnSend -> {
                val msg : String  = binding.txtMessage.text.toString()
                listData.add(listData.size, MessageModel(msg, true, System.currentTimeMillis() , "sama"))
                binding.messageList.adapter?.notifyItemInserted(listData.size)
                val gson = Gson()
                val gsonPretty = GsonBuilder().setPrettyPrinting().create()
                sharedViewModel.publishMessage(roomKey, gsonPretty.toJson(listData[listData.size -1]))
            }
        }
    }
}