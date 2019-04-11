package pw.vasilis.android.SodaqTemperatureLora;

import android.util.Log;

import com.robinhood.spark.SparkAdapter;

import java.util.List;



public class TemperatureAdapter extends SparkAdapter {

    List<Payload> mPayloads;

    public TemperatureAdapter(List<Payload> payloads) {
        mPayloads = payloads;
    }

    @Override
    public int getCount() {
        return mPayloads.size();
    }

    @Override
    public Object getItem(int index) {
        return mPayloads.get(index).getmTemp().floatValue();
    }

    @Override
    public float getY(int index) {
        Log.d("Y",""+mPayloads.get(index).getmTemp().floatValue());

        return mPayloads.get(index).getmTemp().floatValue();
    }
}
