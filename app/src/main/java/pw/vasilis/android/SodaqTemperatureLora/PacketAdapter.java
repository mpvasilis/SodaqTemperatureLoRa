package pw.vasilis.android.SodaqTemperatureLora;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.ttn.android.sample.R;
import org.ttn.android.sdk.v1.domain.Packet;

import java.text.DateFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class PacketAdapter extends RecyclerView.Adapter<PacketAdapter.ViewHolder> {

    List<Packet> mPackets;
    DateFormat mDateFormatter = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.MEDIUM);

    public PacketAdapter(List<Packet> packets) {
        mPackets = packets;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_packet, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        Packet packet = mPackets.get(position);

        // node eui
        String nodeEui = packet.getDevEUI();
        String temperature = String.valueOf(decodeTemperature(packet.getPayload()))+" °C" ;
        if (!TextUtils.isEmpty(nodeEui)) {
            vh.mDeviceId.setVisibility(View.VISIBLE);
            vh.mDeviceId.setText(nodeEui);
        } else {
            vh.mDeviceId.setVisibility(View.GONE);
            vh.mDeviceId.setText(null);
        }

        DateTime date = new DateTime();//packet.getMetadata().get(0).getServerTime()
        if (date != null) {
            vh.mTime.setVisibility(View.VISIBLE);
            vh.mTime.setText(mDateFormatter.format(date.toDate()));
        } else {
            vh.mTime.setVisibility(View.GONE);
            vh.mTime.setText(null);
        }
        vh.mTmperature.setVisibility(View.VISIBLE);
        vh.mTmperature.setText(temperature);
    }

    @Override
    public int getItemCount() {
        return mPackets.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.packet_time) TextView mTime;
        @Bind(R.id.packet_device_id) TextView mDeviceId;
        @Bind(R.id.temperature) TextView mTmperature;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
     double decodeTemperature(String encodedPayload) {
        byte[] bytes = Base64.decode(encodedPayload, Base64.DEFAULT);
         double temp = Double.parseDouble(new String(bytes));
        return temp;
    }

}
