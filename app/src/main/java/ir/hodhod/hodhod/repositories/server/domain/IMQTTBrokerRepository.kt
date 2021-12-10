package ir.hodhod.hodhod.repositories.server.domain

import ir.hodhod.hodhod.repositories.server.persistence.BaseResult
import kotlinx.coroutines.flow.Flow
import org.eclipse.paho.client.mqttv3.IMqttToken

interface IMQTTBrokerRepository {

    suspend fun connect(): BaseResult<IMqttToken, String>
    suspend fun subscribe(topic: String): BaseResult<IMqttToken, String>
    suspend fun publish(topic: String, msg: String): BaseResult<IMqttToken, String>
    suspend fun unsubscribe(topic: String): BaseResult<IMqttToken, String>
    suspend fun disconnect(): BaseResult<IMqttToken, String>
    suspend fun setMessageCallback(): Flow<BaseResult<Any, String>>
}