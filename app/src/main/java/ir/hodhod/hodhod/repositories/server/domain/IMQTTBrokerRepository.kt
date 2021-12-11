package ir.hodhod.hodhod.repositories.server.domain

import androidx.lifecycle.MutableLiveData
import ir.hodhod.hodhod.repositories.server.persistence.BaseResult

interface IMQTTBrokerRepository {

    suspend fun connect(): BaseResult<Unit, String>
    suspend fun subscribe(topic: String): BaseResult<Unit, String>
    suspend fun publish(topic: String, msg: String): BaseResult<Unit, String>
    suspend fun unsubscribe(topic: String): BaseResult<Unit, String>
    suspend fun disconnect(): BaseResult<Unit, String>
    suspend fun setMessageCallback(
        messageDeliver: MutableLiveData<Unit>,
        messageArrived: MutableLiveData<String>,
        connectionLost: MutableLiveData<String>
    )
}