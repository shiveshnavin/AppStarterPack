package in.hoptec.ppower.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import in.hoptec.ppower.utl;


public class AppService extends Service {
    public AppService() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        utl.l("Service Started");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
