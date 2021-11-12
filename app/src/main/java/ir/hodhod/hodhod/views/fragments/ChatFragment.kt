package ir.hodhod.hodhod.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ir.hodhod.hodhod.databinding.FragmentChatBinding

class ChatFragment : Fragment(), View.OnClickListener {

    // region of params
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
    }

    private fun initialView() {
        binding.chatTitleTextView.text = roomKey

        binding.textViewChat.setOnClickListener(this)
        binding.chatBackImageView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.textViewChat,
            binding.chatBackImageView -> navController.navigateUp()
        }
    }
}