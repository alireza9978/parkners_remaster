package me.coleo.snapion.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.wooplr.spotlight.SpotlightConfig;
import com.wooplr.spotlight.utils.SpotlightSequence;

import org.neshan.core.Bounds;
import org.neshan.core.LngLat;
import org.neshan.layers.VectorElementLayer;
import org.neshan.services.NeshanMapStyle;
import org.neshan.services.NeshanServices;
import org.neshan.ui.ClickData;
import org.neshan.ui.MapEventListener;
import org.neshan.ui.MapView;

import java.util.Timer;
import java.util.TimerTask;

import me.coleo.snapion.R;
import me.coleo.snapion.constants.Constants;

/**
 * صفحه اصلی نقشه
 */
public class MainActivity extends AppCompatActivity {

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 1000;

    private static final String TAG = MainActivity.class.getName();

    private MapView map;
    private Button search;
    private Button findLocation;
    private Button searchBar;
    private ImageButton pinButton;
    private ImageButton logoButton;
    private ConstraintLayout bottomLayout;
    final Context context = this;
    final int REQUEST_CODE = 123;
    private Location userLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private boolean permissionToastShown = false;
    private boolean permissionGranted;
    private LocationCallback locationCallback;
    private SettingsClient settingsClient;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionToastShown = false;
        if (requestCode == Constants.MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionGranted = true;
                Constants.getLocationPermission(context, this);
                findLocation.performClick();
            } else {
                Toast.makeText(context, "جهت موقعیت یابی ، برنامه نیاز به دسترسی به موقعیت مکانی دارد.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        map = findViewById(R.id.map);
        map.getLayers().add(NeshanServices.createBaseMap(NeshanMapStyle.STANDARD_DAY));
        focusOnUserLocation();

        initLayoutReferences();
        initLocation();

        Activity activity = this;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SpotlightConfig config = new SpotlightConfig();
                        config.setIntroAnimationDuration(800);
                        config.setRevealAnimationEnabled(true);
                        config.setPerformClick(true);
                        config.setTypeface(ResourcesCompat.getFont(getBaseContext(), R.font.vazir));
                        config.setFadingTextDuration(800);
                        config.setHeadingTvColor(Color.parseColor("#ffffff"));
                        config.setHeadingTvSize(30);
                        config.setSubHeadingTvColor(Color.parseColor("#dddddd"));
                        config.setSubHeadingTvSize(16);
                        config.setMaskColor(Color.parseColor("#dd3a3535"));
                        config.setLineAnimationDuration(400);
                        config.setLineAndArcColor(Color.parseColor("#4149D8"));
                        config.setDismissOnTouch(true);
                        config.setDismissOnBackpress(true);

                        SpotlightSequence.getInstance(activity, config)
                                .addSpotlight(pinButton, "پارکینگ‌های اطراف تو", "مقصدت رو بزن و وضعیت لحظه ای پارکینگ های اطرافش رو ببین", "1")
                                .addSpotlight(search, "جستجو کن", "پارکینگ‌ مدنظرت رو جستوجو کن", "2")
                                .addSpotlight(findLocation, "ببین کجایی", "موقعیت مکانی‌ات رو روی نقشه ببین", "3")
                                .addSpotlight(logoButton, "نظرتو بگو", "از اینجا با ما در ارتباط باش", "4")
                                .startSequence();
                    }
                });


            }
        };
        Timer t = new Timer();
        t.schedule(task, 2000);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    // Initializing layout references (views, map and map events)
    private void initLayoutReferences() {
        // Initializing views
        initViews();
        // Initializing mapView element
        initMap();
        initListener();
        // when long clicked on map, a marker is added in clicked location
        // MapEventListener gets all events on map, including single tap, double tap, long press, etc
        // we should check event type by calling getClickType() on mapClickInfo (from ClickData class)
        map.setMapEventListener(new MapEventListener() {

//            @Override
//            public void onMapStable() {
//                super.onMapStable();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        pinTV.setScaleX(1.5f);
//                        pinTV.setScaleY(1.5f);
//                        work = true;
//                    }
//                });
//
//            }
//
//            private boolean work = true;
//
//            @Override
//            public void onMapMoved() {
//                super.onMapMoved();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (work) {
//                            pinTV.setScaleX(1f);
//                            pinTV.setScaleY(1f);
//                            work = false;
//                        }
//                    }
//                });
//            }

            @Override
            public void onMapClicked(ClickData mapClickInfo) {
                super.onMapClicked(mapClickInfo);
            }

            /*            @Override
            public void onMapClicked(ClickData mapClickInfo) {
                if (mapClickInfo.getClickType() == ClickType.CLICK_TYPE_LONG) {
                    // by calling getClickPos(), we can get position of clicking (or tapping)
                    LngLat clickedLocation = mapClickInfo.getClickPos();
                    // addMarker adds a marker (pretty self explanatory :D) to the clicked location
                    writeStringifiedAddress(clickedLocation.getY(),clickedLocation.getX());
                    addMarker(clickedLocation);
                }
            }
            */
        });
    }

    /**
     * مقدار دهی اولیه مکان کاربر
     */
    private void initLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        settingsClient = LocationServices.getSettingsClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (userLocation == null) {
                    userLocation = locationResult.getLastLocation();
                }
                userLocation = locationResult.getLastLocation();

                focusOnUserLocation();
                stopLocationUpdates();
            }
        };

        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        locationSettingsRequest = builder.build();

    }

    public void startReceivingLocationUpdates() {
        startLocationUpdates();
    }

    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        settingsClient
                .checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(this, locationSettingsResponse -> fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper()))
                .addOnFailureListener(this, e -> {
                    int statusCode = ((ApiException) e).getStatusCode();
                    if (statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        try {
                            ResolvableApiException rae = (ResolvableApiException) e;
                            rae.startResolutionForResult(MainActivity.this, REQUEST_CODE);
                        } catch (IntentSender.SendIntentException ignored) {
                        }
                    }
                });
    }

    /**
     * ساختن اولیه شی نقشه
     */
    private void initMap() {
        VectorElementLayer userMarkerLayer = NeshanServices.createVectorElementLayer();

        LngLat focalPoint = new LngLat(59.505042, 36.326891);
        map.setFocalPointPosition(focalPoint, 0f);
        map.setZoom(17f, 0.5f);
        map.getLayers().add(NeshanServices.createBaseMap(NeshanMapStyle.STANDARD_DAY));

        map.getOptions().setPanBounds(new Bounds(
                new LngLat(43.505859, 24.647017),
                new LngLat(63.984375, 40.178873))
        );

        map.getLayers().add(userMarkerLayer);


    }

    /**
     * مقدار دهی اولیه listners
     */
    private void initListener() {
        pinButton.setOnClickListener(v -> {
            LngLat latLng = map.getFocalPointPosition();
            openSearchPage(latLng.getX(), latLng.getY());
        });
        searchBar.setOnClickListener(v -> openSearchPage());
        search.setOnClickListener(v -> openSearchPage());
        findLocation.setOnClickListener(v -> getLastLocation());
        logoButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SupportActivity.class);
            startActivity(intent);
        });
    }

    /**
     * مقدار دهی اولیه شی های ui
     */
    private void initViews() {
        map = findViewById(R.id.map);
        findLocation = findViewById(R.id.find_location);
        search = findViewById(R.id.search_button);
        searchBar = findViewById(R.id.search_box_edit_text);
        bottomLayout = findViewById(R.id.search_bar_container);
        pinButton = findViewById(R.id.pin_button);
        logoButton = findViewById(R.id.parkners_icon);
    }

    /**
     * تغییر موقعیت نقشه به مکان کاربر
     */
    public void focusOnUserLocation() {
        if (userLocation != null) {
            map.setFocalPointPosition(
                    new LngLat(userLocation.getLongitude(), userLocation.getLatitude()), 0.25f);
        }
    }

    /**
     * گرفتن اخرین مکان کاربر
     */
    private void getLastLocation() {

        permissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (!permissionGranted) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        } else {

            if (Constants.isLocationEnabled(this)) {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, locationSettingsResponse ->
                                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper()));
            } else {
                Toast.makeText(context, "جهت موقعیت یابی ، برنامه نیاز به دسترسی به موقعیت مکانی دارد.", Toast.LENGTH_LONG).show();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                };
                Timer timer = new Timer();
                timer.schedule(timerTask, 1000);
            }


        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    /**
     * جلوگیری از بررسی تغیرات مکان کاربر
     */
    public void stopLocationUpdates() {
        fusedLocationClient
                .removeLocationUpdates(locationCallback)
                .addOnCompleteListener(this, task -> {
                });
    }

    /**
     * تغییر صفحه به جستوحو
     */
    private void openSearchPage(double lat, double lng) {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        intent.putExtra(Constants.SEARCH_MODE, Constants.SearchMode.location);
        intent.putExtra(Constants.SEARCH_LAT, lat);
        intent.putExtra(Constants.SEARCH_LNG, lng);

        startTransition(intent);
    }

    /**
     * تغییر صفحه به جستوحو
     */
    private void openSearchPage() {
        Intent i = new Intent(MainActivity.this, SearchActivity.class);
        i.putExtra(Constants.SEARCH_MODE, Constants.SearchMode.search);
        if (userLocation != null) {
            i.putExtra(Constants.HAVE_lATLNG, true);
            i.putExtra(Constants.SEARCH_LAT, userLocation.getLatitude());
            i.putExtra(Constants.SEARCH_LNG, userLocation.getLongitude());
        } else {
            i.putExtra(Constants.HAVE_lATLNG, false);
        }
        startTransition(i);
    }

    /**
     * افزودن انیمیشن تغیر صفحه
     */
    private void startTransition(Intent intent) {
        String transitionNameEdit = getString(R.string.search_bar_transition_name);
        String transitionNameButton = getString(R.string.search_button_transition_name);
        String transitionNameLocation_back = getString(R.string.location_back_transition_name);
        String transitionNameContainer = getString(R.string.main_container_transition_name);

        Pair<View, String> p1 = new Pair<>(searchBar, transitionNameEdit);
        Pair<View, String> p2 = new Pair<>(search, transitionNameButton);
        Pair<View, String> p3 = new Pair<>(findLocation, transitionNameLocation_back);
        Pair<View, String> p4 = new Pair<>(bottomLayout, transitionNameContainer);

        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, p1, p2, p3, p4);
        startActivity(intent, transitionActivityOptions.toBundle());
    }

    public boolean isPermissionToastShown() {
        return permissionToastShown;
    }

    public void setPermissionToastShown(boolean permissionToastShown) {
        this.permissionToastShown = permissionToastShown;
    }

}
