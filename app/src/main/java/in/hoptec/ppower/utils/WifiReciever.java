package in.hoptec.ppower.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by shivesh on 2/1/18.
 */

public class WifiReciever extends BroadcastReceiver {

    public void alreadyConnected()
    {




    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();
        if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI)
            Log.d("WifiReceiver", "Have Wifi Connection");
        else
            Log.d("WifiReceiver", "Don't have Wifi Connection");
    }
};
