package me.coleo.snapion.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

        Button findLocation = findViewById(R.id.find_location);
        final Button search = findViewById(R.id.search_button);
        final EditText searchBar = findViewById(R.id.search_box_edit_text);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, SearchActivity.class);

                String transitionNameEdit = getString(R.string.search_bar_transition_name);
                String transitionNameButton = getString(R.string.search_button_transition_name);

                Pair<View, String> p1 = new Pair<>((View) searchBar, transitionNameEdit);
                Pair<View, String> p2 = new Pair<>((View) search, transitionNameButton);

                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, p1, p2);
                startActivity(i, transitionActivityOptions.toBundle());

            }
        });

    }

}
