package me.coleo.snapion.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.neshan.core.LngLat;
import org.neshan.services.NeshanMapStyle;
import org.neshan.services.NeshanServices;
import org.neshan.ui.MapView;

import me.coleo.snapion.R;

public class MainActivity extends AppCompatActivity {

    private MapView map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        map = findViewById(R.id.map);
        LngLat focalPoint = new LngLat(59.6168, 36.2605);
        map.setFocalPointPosition(focalPoint, 0f);
        map.setZoom(14f, 0.5f);
        map.getLayers().add(NeshanServices.createBaseMap(NeshanMapStyle.STANDARD_DAY));
    }

}
