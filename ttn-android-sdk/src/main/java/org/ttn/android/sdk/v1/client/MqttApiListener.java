package org.ttn.android.sdk.v1.client;


import org.ttn.android.sdk.v1.domain.Packet;


public interface MqttApiListener {
    void onPacket(Packet packet);

    void onError(Throwable throwable);

    void onConnected();

    void onDisconnected();
}
