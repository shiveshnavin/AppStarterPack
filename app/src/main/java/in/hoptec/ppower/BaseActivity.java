package in.hoptec.ppower;

import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by shivesh on 7/1/18.
 */

public class BaseActivity  extends AppCompatActivity {




    public String getstring(@StringRes int res)
    {


    return getResources().getString(res);

    }








}
