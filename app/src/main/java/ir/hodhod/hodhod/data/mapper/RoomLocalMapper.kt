package ir.hodhod.hodhod.data.mapper

import ir.hodhod.hodhod.data.db.entities.RoomEntity
import ir.hodhod.hodhod.data.models.RoomModel

object RoomLocalMapper {

    fun toDomain(roomEntity: RoomEntity) = roomEntity.run {
        RoomModel(roomKey, time)
    }

    fun fromDomain(roomModel: RoomModel) = roomModel.run {
        RoomEntity(key, time)
    }
}