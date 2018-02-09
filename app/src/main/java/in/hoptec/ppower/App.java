package in.hoptec.ppower;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import in.hoptec.ppower.data.Constants;

/**
 * Created by shivesh on 9/2/18.
 */

public class App extends Application {


    public static FirebaseUser firebaseUser;
    public static  FirebaseAuth mAuth;
    public static GoogleApiClient mGoogleApiClient;


    private static App mInstanceApplication;
    private static Context mContextApplication;

    public void onCreate(){
        Constants.init(this);
        super.onCreate();

        mContextApplication = getApplicationContext();
    }

    public static Context getAppContext(){
        return mContextApplication;
    }
}
