package pw.vasilis.android.SodaqTemperatureLora;

import android.util.Base64;


class Payload {


    Double mTemp;

    public Payload(Double temp) {
        mTemp = temp;
    }

    static Payload fromEncodedPayload(String encodedPayload) {
        byte[] bytes = Base64.decode(encodedPayload, Base64.DEFAULT);
        double temp = Double.parseDouble(new String(bytes));
        return new Payload(temp);
    }

    public Double getmTemp() {
        return mTemp;
    }

}

