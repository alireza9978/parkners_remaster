package me.coleo.snapion.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.neshan.core.Bounds;
import org.neshan.core.LngLat;
import org.neshan.layers.VectorElementLayer;
import org.neshan.services.NeshanMapStyle;
import org.neshan.services.NeshanServices;
import org.neshan.styles.AnimationStyle;
import org.neshan.styles.AnimationStyleBuilder;
import org.neshan.styles.AnimationType;
import org.neshan.styles.MarkerStyle;
import org.neshan.styles.MarkerStyleCreator;
import org.neshan.ui.MapView;
import org.neshan.utils.BitmapUtils;
import org.neshan.vectorelements.Marker;

import me.coleo.snapion.R;
import me.coleo.snapion.constants.Constants;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    private MapView map;
    private Button search;
    private Button findLocation;
    private Button searchBar;
    private ImageButton pinButton;
    private ConstraintLayout bottomLayout;

    private VectorElementLayer userMarkerLayer;

    // User's current location
    private Location userLocation;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initViews();
        initListener();
        initMap();
        initLocation();

    }

    private void initMap() {
        userMarkerLayer = NeshanServices.createVectorElementLayer();

        LngLat focalPoint = new LngLat(59.6168, 36.2605);
        map.setFocalPointPosition(focalPoint, 0f);
        map.setZoom(14f, 0.5f);
        map.getLayers().add(NeshanServices.createBaseMap(NeshanMapStyle.STANDARD_DAY));

        map.getOptions().setPanBounds(new Bounds(
                new LngLat(43.505859, 24.647017),
                new LngLat(63.984375, 40.178873))
        );

        map.getLayers().add(userMarkerLayer);
    }

    private void initListener() {
        pinButton.setOnClickListener(v -> openSearchPage());
        searchBar.setOnClickListener(v -> openSearchPage());
        search.setOnClickListener(v -> openSearchPage());
        findLocation.setOnClickListener(v -> getLastLocation());
    }

    private void initViews() {
        map = findViewById(R.id.map);
        findLocation = findViewById(R.id.find_location);
        search = findViewById(R.id.search_button);
        searchBar = findViewById(R.id.search_box_edit_text);
        bottomLayout = findViewById(R.id.search_bar_container);
        pinButton = findViewById(R.id.pin_button);
    }

    private void initLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void getLastLocation() {
        fusedLocationClient
                .getLastLocation()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        onLocationChange(task.getResult());
                        Log.i(TAG, "lat " + task.getResult().getLatitude() + " lng " + task.getResult().getLongitude());
                    } else {
                        Toast.makeText(MainActivity.this, "موقعیت یافت نشد.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void onLocationChange(Location location) {
        this.userLocation = location;
        addUserMarker(new LngLat(userLocation.getLongitude(), userLocation.getLatitude()));
        map.setFocalPointPosition(
                new LngLat(userLocation.getLongitude(), userLocation.getLatitude()),
                0.25f);
        map.setZoom(15, 0.25f);
    }


    private void addUserMarker(LngLat loc) {
        userMarkerLayer.clear();

        AnimationStyleBuilder animStBl = new AnimationStyleBuilder();
        animStBl.setFadeAnimationType(AnimationType.ANIMATION_TYPE_SMOOTHSTEP);
        animStBl.setSizeAnimationType(AnimationType.ANIMATION_TYPE_SPRING);
        animStBl.setPhaseInDuration(0.5f);
        animStBl.setPhaseOutDuration(0.5f);
        AnimationStyle animSt = animStBl.buildStyle();

        MarkerStyleCreator markStCr = new MarkerStyleCreator();
        markStCr.setSize(20f);
        markStCr.setBitmap(BitmapUtils.createBitmapFromAndroidBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_location_pin)));
        markStCr.setAnimationStyle(animSt);
        MarkerStyle markSt = markStCr.buildStyle();

        Marker marker = new Marker(loc, markSt);

        userMarkerLayer.add(marker);
    }

    private void openSearchPage(double lat, double lng) {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        intent.putExtra(Constants.SEARCH_MODE, Constants.SearchMode.search);
        startActivity(intent);
    }

    private void openSearchPage() {
        Intent i = new Intent(MainActivity.this, SearchActivity.class);
        i.putExtra(Constants.SEARCH_MODE, Constants.SearchMode.search);

        String transitionNameEdit = getString(R.string.search_bar_transition_name);
        String transitionNameButton = getString(R.string.search_button_transition_name);
        String transitionNameLocation_back = getString(R.string.location_back_transition_name);
        String transitionNameContainer = getString(R.string.main_container_transition_name);

        Pair<View, String> p1 = new Pair<>(searchBar, transitionNameEdit);
        Pair<View, String> p2 = new Pair<>(search, transitionNameButton);
        Pair<View, String> p3 = new Pair<>(findLocation, transitionNameLocation_back);
        Pair<View, String> p4 = new Pair<>(bottomLayout, transitionNameContainer);

        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, p1, p2, p3, p4);
        startActivity(i, transitionActivityOptions.toBundle());
    }

}
