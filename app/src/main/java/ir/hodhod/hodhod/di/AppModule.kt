package ir.hodhod.hodhod.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.hodhod.hodhod.Constants
import ir.hodhod.hodhod.repositories.server.persistence.MQTTBrokerRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.eclipse.paho.client.mqttv3.MqttAsyncClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMQTTClient() =
        MqttAsyncClient(Constants.SERVER_URL, "sepehr")

    @ExperimentalCoroutinesApi
    @Singleton
    @Provides
    fun provideMQTTBrokerRepository(mqttAsyncClient: MqttAsyncClient) =
        MQTTBrokerRepository(mqttAsyncClient)
}