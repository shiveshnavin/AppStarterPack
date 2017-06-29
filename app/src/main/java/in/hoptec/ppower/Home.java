package in.hoptec.ppower;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
    }
}
