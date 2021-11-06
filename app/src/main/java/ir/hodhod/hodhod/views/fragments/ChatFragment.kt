package ir.hodhod.hodhod.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ir.hodhod.hodhod.databinding.FragmentChatBinding

class ChatFragment : Fragment() {

    // region of params
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    // END of region of params

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

        initialView()
    }

    private fun initialView() {
        binding.textViewChat.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}