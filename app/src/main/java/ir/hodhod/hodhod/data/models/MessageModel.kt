package ir.hodhod.hodhod.data.models

data class MessageModel(
    val content: String,
    val fromMe: Boolean,
    val time: Long,
    val username: String
)