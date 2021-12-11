package ir.hodhod.hodhod.repositories.server

import ir.hodhod.hodhod.repositories.server.persistence.BaseResult
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken


/**
 * this extension function can be reduce boilerplate code
 *
 * @param block will be execute related method
 * @param onSuccess will be called whenever `block` is run successfully
 * @param onError will be called whenever exception or error occur
 *
 * @return [IMqttToken] that is returned by `block` if it is run successfully, `null` otherwise.
 * */
inline fun MqttAndroidClient.executeMethod(
    block: MqttAndroidClient.() -> IMqttToken,
    crossinline onSuccess: () -> BaseResult.Success<Unit>,
    crossinline onError: (String?) -> BaseResult.Error<String>
): BaseResult<Unit, String> {

    try {
        block().apply {

            actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    onSuccess()
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    exception?.printStackTrace()
                    onError(exception?.message)
                }
            }
        }

        return BaseResult.Success(Unit)

    } catch (e: Exception) {
        e.printStackTrace()
        onError(e.message)

        return BaseResult.Error(e.message)
    }
}
