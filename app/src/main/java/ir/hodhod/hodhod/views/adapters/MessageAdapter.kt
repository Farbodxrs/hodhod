package ir.hodhod.hodhod.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ir.hodhod.hodhod.R
import ir.hodhod.hodhod.data.models.MessageModel
import ir.hodhod.hodhod.utils.DateUtil
import ir.hodhod.hodhod.utils.UsernameSharedPreferences


class MessageAdapter(
    private val dataset: List<MessageModel>
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.my_message)
        val otherTextView: TextView = itemView.findViewById(R.id.other_message)

        //        val date: TextView = view.findViewById((R.id.date))
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

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val item = dataset[position]
        val userPreference by lazy {
            UsernameSharedPreferences.initialWith(holder.itemView.context)
        }

        if (item.username == userPreference.getUsername()) {
            holder.apply {
                textView.text = item.content
//                  date.text = DateUtil.formatDate(item.createdAt)
                meTimestamps.text = DateUtil.formatTime(item.time)
                otherTextView.visibility = View.GONE
                username.visibility = View.GONE
                otherTimestamps.visibility = View.GONE
            }

        } else {
            holder.apply {
                otherTextView.text = item.content
                otherTimestamps.text = DateUtil.formatTime(item.time)
//                  date.text = DateUtil.formatDate(item.createdAt)
                username.text = item.username
                meTimestamps.visibility = View.GONE
                textView.visibility = View.GONE
            }
        }
    }
}