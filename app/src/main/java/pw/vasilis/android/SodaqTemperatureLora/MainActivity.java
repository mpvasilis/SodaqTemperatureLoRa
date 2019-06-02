package pw.vasilis.android.SodaqTemperatureLora;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.robinhood.spark.SparkView;

import org.ttn.android.sample.R;
import org.ttn.android.sdk.v1.client.MqttApiListener;
import org.ttn.android.sdk.v1.client.TTNMqttClient;
import org.ttn.android.sdk.v1.domain.Packet;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String APP_EUI = "uowm_iot_temperature";
    private static final String ACCESS_KEY = "ttn-account-v2.2WxPN6PYFuZXrQZVFDGWmQf2npGOblJQsX5WFzaSqMs";
    private static final String STAGING_HOST = "eu.thethings.network";

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.packet_list) RecyclerView mDataList;
    @Bind(R.id.temperature_view) SparkView mTempView;
    @Bind(R.id.progress_bar) CircleProgressBar mProgressBar;

    TTNMqttClient mTTNMqttClient;

    final List<Packet> mPackets = new ArrayList<>();
    final List<Payload> mPayloads = new ArrayList<>();

    final PacketAdapter mPacketAdapter = new PacketAdapter(mPackets);
    final TemperatureAdapter mTemperatureAdapter = new TemperatureAdapter(mPayloads);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);

       // mPackets.addAll(SampleData.mSamplePackets);
        //mPayloads.addAll(SampleData.mSamplePayloads);


        mDataList.setLayoutManager(new LinearLayoutManager(this));
        mDataList.setAdapter(mPacketAdapter);

        mTempView.setAdapter(mTemperatureAdapter);


    }

    @Override
    public void onStart() {
        super.onStart();

        subscribe();
    }

    @Override
    public void onStop() {
        super.onStop();

        mTTNMqttClient.disconnect();
    }


    void subscribe() {
        mProgressBar.setVisibility(View.VISIBLE);

        mTTNMqttClient = new TTNMqttClient(STAGING_HOST, APP_EUI, ACCESS_KEY, "+");

        mTTNMqttClient.listen(new MqttApiListener() {
            @Override
            public void onPacket(final Packet packet) {
                Log.d(TAG, "onPacket");
                toastOnUiThread(getString(R.string.packet_received));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPackets.add(0, packet);
                        mPacketAdapter.notifyItemInserted(0);
                        mDataList.scrollToPosition(0);

                        Payload payload = Payload.fromEncodedPayload(packet.getPayload());
                        mPayloads.add(payload);
                        mTemperatureAdapter.notifyDataSetChanged();

                    }
                });
            }

            @Override
            public void onError(final Throwable throwable) {
                Log.e(TAG, "onError: " + throwable.getMessage());
                toastOnUiThread(getString(R.string.mqtt_error, throwable.getMessage()));
            }

            @Override
            public void onConnected() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "onConnected");
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onDisconnected() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "onDisconnected");
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    void toastOnUiThread(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

}
