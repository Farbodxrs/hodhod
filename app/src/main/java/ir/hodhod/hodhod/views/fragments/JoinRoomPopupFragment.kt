package ir.hodhod.hodhod.views.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ir.hodhod.hodhod.R

class JoinRoomPopupFragment : DialogFragment() {

    // region of params
    private lateinit var listener: JoinClickListener

    // END of region of params

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?) = requireActivity().let {
        val dialogView = it.layoutInflater.inflate(R.layout.fragment_join_room_popup, null)
        AlertDialog.Builder(it).run {
            setButtonsClickListeners(dialogView)
            setView(dialogView)

            create()
        }
    }

    private fun setButtonsClickListeners(view: View) {
        with(view) {

            val cancelButton = findViewById<Button>(R.id.joinButtonCancel)
            val joinButton = findViewById<Button>(R.id.joinButtonEnter)
            val keyEditText = findViewById<EditText>(R.id.joinEditText)

            cancelButton.setOnClickListener { dismiss() }

            joinButton.setOnClickListener {
                val key = keyEditText.text.toString()
                if (key.isBlank() || key.contains(" ") || !key.matches(Regex("[a-zA-Z0-9]+")))
                    Toast.makeText(requireContext(), R.string.enter_key_toast, Toast.LENGTH_SHORT)
                        .show()
                else {
                    listener.onJoinClickListener(key)
                    dismiss()
                }
            }
        }
    }

    interface JoinClickListener {
        fun onJoinClickListener(key: String)
    }

    companion object {
        const val JOIN_POPUP_TAG = "joinPopupTag"

        fun getInstance(listener: JoinClickListener) =
            JoinRoomPopupFragment().apply {
                this.listener = listener
            }
    }
}