package ir.hodhod.hodhod.data.models

import com.google.android.gms.maps.model.LatLng

data class MessageModel(
    val content: String,
    val time: Long,
    val username: String,
    val roomKey: String,
    val location: LatLng? = null
)