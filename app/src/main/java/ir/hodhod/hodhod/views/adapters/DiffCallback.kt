package ir.hodhod.hodhod.views.adapters

import androidx.recyclerview.widget.DiffUtil
import ir.hodhod.hodhod.data.models.MessageModel

class GeneralDiffCallback(
    private val list: List<MessageModel>,
    private val newList: List<MessageModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = list.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return list[oldItemPosition].time == newList[newItemPosition].time
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return list[oldItemPosition].time == newList[newItemPosition].time
    }
}