package ir.hodhod.hodhod.views.adapters

import android.R.attr
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ir.hodhod.hodhod.R
import ir.hodhod.hodhod.models.MessageModel
import java.text.SimpleDateFormat
import java.util.*
import android.R.attr.data





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

        if (item.fromMe) {
            holder.textView.text = item.content
//            holder.date.text = DateUtil.formatDate(item.createdAt)
            holder.meTimestamps.text = DateUtil.formatTime(item.time)
            holder.otherTextView.visibility = View.GONE
            holder.username.visibility = View.GONE
            holder.otherTimestamps.visibility = View.GONE

        } else {
            holder.otherTextView.text = item.content
            holder.otherTimestamps.text = DateUtil.formatTime(item.time)
//            holder.date.text = DateUtil.formatDate(item.createdAt)
            holder.username.text = item.username
            holder.meTimestamps.visibility = View.GONE
            holder.textView.visibility = View.GONE
        }
    }


    object DateUtil {
        fun formatTime(timeInMillis: Long): String {
            val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            return dateFormat.format(timeInMillis)
        }


        fun formatDate(timeInMillis: Long): String {
            val dateFormat = SimpleDateFormat("MMMM dd", Locale.getDefault())
            return dateFormat.format(timeInMillis)
        }
    }

}