package ir.hodhod.hodhod.views.data


import ir.hodhod.hodhod.views.model.Message

class Datasource() {

    fun loadMessages(): List<Message> {
        return listOf<Message>(
            Message("این پیام برای تست است.", false),
            Message("باشه.", false),
            Message("سلام به همگی", true),
            Message("سلام", false)
        )
    }
}