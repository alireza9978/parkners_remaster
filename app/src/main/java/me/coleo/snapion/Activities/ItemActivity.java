package me.coleo.snapion.Activities;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

//            ItemSliderAdapter itemSliderAdapter = new ItemSliderAdapter(parking.getImageURLs());
            ItemSliderAdapter itemSliderAdapter = new ItemSliderAdapter();

            slider.setAdapter(itemSliderAdapter);
            slider.setSelectedSlide(itemSliderAdapter.getItemCount() - 1, false);

            initMap(parking.getAddress_latitude(), parking.getAddress_longitude());

        } catch (NullPointerException ignored) {
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
        markStCr.setSize(45f);
        markStCr.setBitmap(BitmapUtils.createBitmapFromAndroidBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.logo)));

        markStCr.setAnimationStyle(animSt);
        MarkerStyle markSt = markStCr.buildStyle();
        Marker temp = new Marker(focalPoint, markSt);
        userMarkerLayer.add(temp);


    }

}
