package org.ttn.android.sdk.v1.client;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.Listener;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;
import org.joda.time.DateTime;
import org.ttn.android.sdk.v1.api.DateTimeConverter;
import org.ttn.android.sdk.v1.domain.Packet;

import java.net.URISyntaxException;


public class TTNMqttClient {
    private static final int MQTT_HOST_PORT = 1883;

    final Gson mGson;
    final Topic mTopic;
    final MQTT mMqtt = new MQTT();
    CallbackConnection mConnection;

    public TTNMqttClient(String broker, String appEUI, String accessKey, String devEUI) {
        mGson = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeConverter())
                .create();
        mTopic = new Topic(appEUI + "/devices/" + devEUI + "/up", QoS.AT_LEAST_ONCE);
        try {
            mMqtt.setHost("tcp://" + broker + ":" + MQTT_HOST_PORT);
            mMqtt.setUserName(appEUI);
            mMqtt.setPassword(accessKey);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void listen(final MqttApiListener listener) {

        // establish connection
        mConnection = mMqtt.callbackConnection();
        mConnection.listener(new Listener() {
            @Override
            public void onConnected() {
                listener.onConnected();
            }

            @Override
            public void onDisconnected() {
                listener.onDisconnected();
            }

            @Override
            public void onPublish(UTF8Buffer topic, Buffer body, Runnable ack) {

                try {
                    String jsonStr = body.ascii().toString();
                    Log.d("json",jsonStr);
                    Packet packet = mGson.fromJson(jsonStr, Packet.class);
                    listener.onPacket(packet);
                } catch (JsonSyntaxException e) {
                    listener.onError(e);
                }

                ack.run();
            }

            @Override
            public void onFailure(Throwable value) {
                listener.onError(value);
            }
        });

        // connect
        mConnection.connect(new Callback<Void>() {
            @Override
            public void onFailure(Throwable value) {
                listener.onError(value);
            }

            @Override
            public void onSuccess(Void value) {

                // subscribe
                mConnection.subscribe(new Topic[]{mTopic}, new Callback<byte[]>() {
                    public void onSuccess(byte[] qoses) {
                        // The result of the subscribe request.
                    }

                    public void onFailure(Throwable value) {
                        listener.onError(value);
                    }
                });
            }

        });

    }

    public void disconnect() {
        if (mConnection != null) {
            mConnection.disconnect(new Callback<Void>() {
                @Override
                public void onSuccess(Void value) {
                }

                @Override
                public void onFailure(Throwable value) {

                }
            });
        }
    }
}
