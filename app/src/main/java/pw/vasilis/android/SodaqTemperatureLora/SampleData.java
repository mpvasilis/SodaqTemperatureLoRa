package pw.vasilis.android.SodaqTemperatureLora;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;
import org.ttn.android.sdk.v1.api.DateTimeConverter;
import org.ttn.android.sdk.v1.domain.Packet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SampleData {
    static public List<Packet> mSamplePackets = new ArrayList<>();
    static public List<Payload> mSamplePayloads = new ArrayList<>();

    static {
        String[] ar = new String[]{
                "{\"app_id\":\"uowm_iot_temperature\",\"dev_id\":\"sodaq\",\"hardware_serial\":\"0004A30B002014E2\",\"port\":1,\"counter\":9,\"payload_raw\":\"MjcuNDI=\",\"metadata\":{\"time\":\"2019-04-08T13:17:20.520075197Z\",\"frequency\":868.1,\"modulation\":\"LORA\",\"data_rate\":\"SF9BW125\",\"airtime\":185344000,\"coding_rate\":\"4/5\",\"gateways\":[{\"gtw_id\":\"ttn-kozani-icte\",\"gtw_trusted\":true,\"timestamp\":3571108588,\"time\":\"2019-04-08T13:17:20Z\",\"channel\":0,\"rssi\":-66,\"snr\":11.5,\"rf_chain\":1,\"latitude\":40.30632,\"longitude\":21.807411,\"altitude\":710,\"location_source\":\"registry\"}],\"latitude\":40.305542,\"longitude\":21.803896,\"location_source\":\"registry\"}}",

                "{\"app_id\":\"uowm_iot_temperature\",\"dev_id\":\"sodaq\",\"hardware_serial\":\"0004A30B002014E2\",\"port\":1,\"counter\":9,\"payload_raw\":\"MjcuNDI=\",\"metadata\":{\"time\":\"2019-04-08T13:17:20.520075197Z\",\"frequency\":868.1,\"modulation\":\"LORA\",\"data_rate\":\"SF9BW125\",\"airtime\":185344000,\"coding_rate\":\"4/5\",\"gateways\":[{\"gtw_id\":\"ttn-kozani-icte\",\"gtw_trusted\":true,\"timestamp\":3571108588,\"time\":\"2019-04-08T13:17:20Z\",\"channel\":0,\"rssi\":-66,\"snr\":11.5,\"rf_chain\":1,\"latitude\":40.30632,\"longitude\":21.807411,\"altitude\":710,\"location_source\":\"registry\"}],\"latitude\":40.305542,\"longitude\":21.803896,\"location_source\":\"registry\"}}",
        "{\"app_id\":\"uowm_iot_temperature\",\"dev_id\":\"sodaq\",\"hardware_serial\":\"0004A30B002014E2\",\"port\":1,\"counter\":9,\"payload_raw\":\"MjcuNDI=\",\"metadata\":{\"time\":\"2019-04-08T13:17:20.520075197Z\",\"frequency\":868.1,\"modulation\":\"LORA\",\"data_rate\":\"SF9BW125\",\"airtime\":185344000,\"coding_rate\":\"4/5\",\"gateways\":[{\"gtw_id\":\"ttn-kozani-icte\",\"gtw_trusted\":true,\"timestamp\":3571108588,\"time\":\"2019-04-08T13:17:20Z\",\"channel\":0,\"rssi\":-66,\"snr\":11.5,\"rf_chain\":1,\"latitude\":40.30632,\"longitude\":21.807411,\"altitude\":710,\"location_source\":\"registry\"}],\"latitude\":40.305542,\"longitude\":21.803896,\"location_source\":\"registry\"}}"};



        List<String> jsonPackets = Arrays.asList(ar);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeConverter())
                .create();
        for (String packetStr : jsonPackets) {
            Packet packet = gson.fromJson(packetStr, Packet.class);
            Payload payload = Payload.fromEncodedPayload(packet.getPayload());
            mSamplePayloads.add(payload);
            mSamplePackets.add(packet);
        }
    }
}
