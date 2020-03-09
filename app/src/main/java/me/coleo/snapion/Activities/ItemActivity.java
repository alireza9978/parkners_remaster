package me.coleo.snapion.Activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import me.coleo.snapion.adapter.ItemImageLoadingService;
import me.coleo.snapion.adapter.ItemSliderAdapter;
import me.coleo.snapion.constants.Constants;
import me.coleo.snapion.models.Parking;
import ss.com.bannerslider.Slider;

public class ItemActivity extends AppCompatActivity {

    TextView addresTV, feeTV, capTV, timesTV, titleTV;
    Slider slider;
    MapView map;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        Slider.init(new ItemImageLoadingService());

        map = findViewById(R.id.map);

        slider = findViewById(R.id.itemSlider);

        addresTV = findViewById(R.id.itemAddrTV);
        feeTV = findViewById(R.id.itemFeeTV);
        titleTV = findViewById(R.id.itemTitleTV);
        timesTV = findViewById(R.id.itemTimesTV);
        capTV = findViewById(R.id.itemCapacityTV);
        ImageButton share = findViewById(R.id.shareButton);
        ImageButton route = findViewById(R.id.route_button);
        ImageButton back = findViewById(R.id.back_arrow);
        back.setOnClickListener(v -> finish());

        Bundle extra = getIntent().getExtras();
        assert extra != null;
        Parking parking;
        try {

            parking = (Parking) extra.getSerializable(Constants.PARKING_ID);
            assert parking != null;

            titleTV.setText(parking.getTitle());
            addresTV.setText(parking.getAddress_text());
            feeTV.setText(parking.getPricesString());
            timesTV.setText(parking.getWorkHoursString());
            capTV.setText(String.valueOf(parking.getTotal_capacity()));

            ItemSliderAdapter itemSliderAdapter = new ItemSliderAdapter(parking.getImageURLs());

            slider.setAdapter(itemSliderAdapter);
            slider.setSelectedSlide(itemSliderAdapter.getItemCount() - 1, false);


            route.setOnClickListener(v -> {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + parking.getAddress_latitude() + "," + parking.getAddress_longitude() + "&mode=d");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);
            });
            share.setOnClickListener(v -> {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "share some info");
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            });

            initMap(parking.getAddress_latitude(), parking.getAddress_longitude());

        } catch (NullPointerException ignored) {
        }


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    private static Bitmap getBitmap(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (drawable instanceof BitmapDrawable) {
            return BitmapFactory.decodeResource(context.getResources(), drawableId);
        } else if (drawable instanceof VectorDrawable) {
            return getBitmap((VectorDrawable) drawable);
        } else {
            throw new IllegalArgumentException("unsupported drawable type");
        }
    }

    private void initMap(float lat, float lang) {
        VectorElementLayer userMarkerLayer = NeshanServices.createVectorElementLayer();

        map.setZoom(14f, 0.5f);
        map.getLayers().add(NeshanServices.createBaseMap(NeshanMapStyle.STANDARD_DAY));

        map.getLayers().add(userMarkerLayer);

        LngLat focalPoint = new LngLat(lang, lat);
        map.setFocalPointPosition(focalPoint, 0f);

        AnimationStyleBuilder animStBl = new AnimationStyleBuilder();
        animStBl.setFadeAnimationType(AnimationType.ANIMATION_TYPE_SMOOTHSTEP);
        animStBl.setSizeAnimationType(AnimationType.ANIMATION_TYPE_SPRING);
        animStBl.setPhaseInDuration(0.5f);
        animStBl.setPhaseOutDuration(0.5f);
        AnimationStyle animSt = animStBl.buildStyle();

        MarkerStyleCreator markStCr = new MarkerStyleCreator();
        markStCr.setSize(20f);

        Bitmap bitmap = getBitmap(getApplicationContext(), R.drawable.ic_marker_red_optimized);

        markStCr.setBitmap(BitmapUtils.createBitmapFromAndroidBitmap(bitmap));
        markStCr.setAnimationStyle(animSt);

        MarkerStyle markSt = markStCr.buildStyle();
        Marker temp = new Marker(focalPoint, markSt);

        userMarkerLayer.add(temp);


    }

}
