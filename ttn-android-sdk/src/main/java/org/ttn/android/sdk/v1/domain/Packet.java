package org.ttn.android.sdk.v1.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Packet {

    /*
    Example incoming uplink packet:

    {
        "payload":"CUUCbw==",
        "port":1,
        "counter":4950,
        "dev_eui":"0004A30B001B442B",
        "metadata":[
            {
                "frequency":868.5,
                "datarate":"SF12BW125",
                "codingrate":"4/5",
                "gateway_timestamp":2899320620,
                "gateway_time":"2016-06-05T08:54:43.376773Z",
                "channel":1,
                "server_time":"2016-06-05T08:54:43.422660261Z",
                "rssi":-111,
                "lsnr":4.5,
                "rfchain":1,
                "crc":1,
                "modulation":"LORA",
                "gateway_eui":"0000024B08060112",
                "altitude":22,
                "longitude":4.88661,
                "latitude":52.37371
        }
        ]
    }

    {
   "app_id":"uowm_iot_temperature",
   "dev_id":"sodaq",
   "hardware_serial":"0004A30B002014E2",
   "port":1,
   "counter":142,
   "payload_raw":"MjcuMTA=",
   "metadata":{
      "time":"2019-04-08T10:31:04.221101377Z",
      "frequency":868.3,
      "modulation":"LORA",
      "data_rate":"SF7BW125",
      "airtime":51456000,
      "coding_rate":"4/5",
      "gateways":[
         {
            "gtw_id":"ttn-kozani-icte",
            "gtw_trusted":true,
            "timestamp":2184763243,
            "time":"2019-04-08T10:31:04Z",
            "channel":1,
            "rssi":-77,
            "snr":9.5,
            "rf_chain":1,
            "latitude":40.30632,
            "longitude":21.807411,
            "altitude":710,
            "location_source":"registry"
         }
      ],
      "latitude":40.305542,
      "longitude":21.803896,
      "location_source":"registry"
   }
}
    */

    @SerializedName("payload_raw")
    String mPayload;

    @SerializedName("port")
    int mPort;

    @SerializedName("counter")
    int mCounter;

    @SerializedName("hardware_serial")
    String mDevEUI;

    //@SerializedName("metadata")
    //List<Metadata> mMetadata;

    @SerializedName("ttl")
    String mTTL;

    public String getPayload() {
        return mPayload;
    }

    public int getPort() {
        return mPort;
    }

    public int getCounter() {
        return mCounter;
    }

    public String getDevEUI() {
        return mDevEUI;
    }

    public List<Metadata> getMetadata() {
        return null;
    }

    public String getTTL() {
        return mTTL;
    }

}

