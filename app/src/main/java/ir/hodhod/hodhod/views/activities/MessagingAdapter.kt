package ir.hodhod.hodhod.views.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ir.hodhod.hodhod.R
import ir.hodhod.hodhod.views.model.Message


class MessagingAdapter(
    private val dataset: List<Message>
) : RecyclerView.Adapter<MessagingAdapter.MessageViewHolder>() {

    class MessageViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.my_message)
        val otherTextView: TextView = itemView.findViewById(R.id.other_message)
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
            holder.otherTextView.visibility = View.GONE
        } else {
            holder.otherTextView.text = item.content
            holder.textView.visibility = View.GONE
        }
    }

}