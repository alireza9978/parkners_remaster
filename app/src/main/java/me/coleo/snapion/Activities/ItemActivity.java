package me.coleo.snapion.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import me.coleo.snapion.R;
import me.coleo.snapion.constants.Constants;

public class ItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Bundle extra = getIntent().getExtras();
        assert extra != null;
        final int id = extra.getInt(Constants.PARKING_ID);
        //todo get parking info

    }


    private void putPinOnMap(int lat,int lang){}

}
