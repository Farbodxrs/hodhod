package ir.hodhod.hodhod.data.mapper

import ir.hodhod.hodhod.data.db.entities.MessageEntity
import ir.hodhod.hodhod.data.models.MessageModel

object MessageLocalMapper {

    fun toDomain(messageEntity: MessageEntity) = messageEntity.run {
        MessageModel(
            content,
            time,
            sender,
            roomKey,
            location
        )
    }

    fun fromDomain(messageModel: MessageModel) = messageModel.run {
        MessageEntity(
            sender = username,
            content = content,
            time = time,
            roomKey = roomKey,
            location = location
        )
    }
}