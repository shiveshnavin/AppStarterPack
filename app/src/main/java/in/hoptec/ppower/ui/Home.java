package in.hoptec.ppower.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import in.hoptec.ppower.R;
import in.hoptec.ppower.utl;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViewById(R.id.activity_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                utl.logout();
                onBackPressed();
            }
        });


        utl.e("User",utl.getUser());
        utl.e("UserData",utl.readUserData());
    }
}
