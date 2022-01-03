package ir.hodhod.hodhod.views.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ir.hodhod.hodhod.R
import ir.hodhod.hodhod.data.models.MessageModel
import ir.hodhod.hodhod.utils.DateUtil
import ir.hodhod.hodhod.utils.UsernameSharedPreferences


class MessageAdapter(
    private var dataset: List<MessageModel>
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.my_message)
        val otherTextView: TextView = itemView.findViewById(R.id.other_message)
        val meDate: TextView = view.findViewById((R.id.date_me))
        val otherDate: TextView = view.findViewById((R.id.date_other))
        val username: TextView = view.findViewById((R.id.username_other))
        val otherTimestamps: TextView = view.findViewById((R.id.timestamp_other))
        val meTimestamps: TextView = view.findViewById((R.id.timestamp_me))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {

        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.message_item, parent, false)
        return MessageViewHolder(adapterLayout)
    }

    override fun getItemCount() = dataset.size

    override fun getItemId(position: Int): Long {
        return dataset[position].time
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val item = dataset[holder.adapterPosition]
        val userPreference by lazy {
            UsernameSharedPreferences.initialWith(holder.itemView.context)
        }

        if (item.username == userPreference.getUsername()) {
            holder.apply {
                textView.text = item.content
                meDate.text = DateUtil.formatDate(item.time)
                meTimestamps.text = DateUtil.formatTime(item.time)
                otherTextView.visibility = View.GONE
                username.visibility = View.GONE
                otherTimestamps.visibility = View.GONE
                otherDate.visibility = View.GONE
            }
        } else {
            holder.apply {
                otherTextView.text = item.content
                otherTimestamps.text = DateUtil.formatTime(item.time)
                otherDate.text = DateUtil.formatDate(item.time)
                username.text = item.username
                meTimestamps.visibility = View.GONE
                textView.visibility = View.GONE
                meDate.visibility = View.GONE
            }
        }
    }

    fun updateData(list: List<MessageModel>) {

        val diffCallback = GeneralDiffCallback(dataset, list)
        Log.e("Sepehr", "hi3 ${dataset.size} | ${dataset.last()}")
        Log.e("Sepehr", "hi4 ${list.size} | ${list.last()}")

        val diffResult = DiffUtil.calculateDiff(diffCallback)

        dataset = list
        diffResult.dispatchUpdatesTo(this)
    }
}