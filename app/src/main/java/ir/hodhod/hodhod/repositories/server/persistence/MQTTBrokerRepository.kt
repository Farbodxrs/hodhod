package ir.hodhod.hodhod.repositories.server.persistence

import android.util.Log
import ir.hodhod.hodhod.repositories.server.domain.IMQTTBrokerRepository
import ir.hodhod.hodhod.repositories.server.executeMethod
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.callbackFlow
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MQTTBrokerRepository @Inject constructor(private val mqttClient: MqttAndroidClient) :
    IMQTTBrokerRepository {

    override suspend fun connect() =
        mqttClient.executeMethod(
            { connect() },
            onSuccess = { Log.d("MQTTBrokerRepository", "connected successfully") },
            onError = { Log.d("MQTTBrokerRepository", "connect failed") })

    override suspend fun subscribe(topic: String) =
        mqttClient.executeMethod(
            { subscribe(topic, 2) },
            onSuccess = { Log.d("MQTTBrokerRepository", "subscribed successfully") },
            onError = { Log.d("MQTTBrokerRepository", "subscribe failed") })

    override suspend fun publish(topic: String, msg: String) =
        mqttClient.executeMethod(
            { publish(topic, msg.toByteArray(), 2, false) },
            onSuccess = { Log.d("MQTTBrokerRepository", "published successfully") },
            onError = { Log.d("MQTTBrokerRepository", "publish failed") })

    override suspend fun unsubscribe(topic: String) =
        mqttClient.executeMethod(
            { unsubscribe(topic) },
            onSuccess = { Log.d("MQTTBrokerRepository", "unsubscribed successfully") },
            onError = { Log.d("MQTTBrokerRepository", "unsubscribe failed") })

    override suspend fun disconnect() =
        mqttClient.executeMethod(
            { disconnect() },
            onSuccess = { Log.d("MQTTBrokerRepository", "disconnected successfully") },
            onError = { Log.d("MQTTBrokerRepository", "disconnect failed") })

    override suspend fun setMessageCallback() = callbackFlow {
        mqttClient.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable?) {
                Log.d("MQTTBrokerRepository", "connection lost -> ${cause?.message}")
                offer(BaseResult.Error(cause?.message))
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                Log.d("MQTTBrokerRepository", "message arrived -> $topic, ${message.toString()}")
                offer(BaseResult.Success(message.toString()))
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                Log.d("MQTTBrokerRepository", "delivery complete")
                offer(BaseResult.Success(Unit))
            }
        })
    }
}