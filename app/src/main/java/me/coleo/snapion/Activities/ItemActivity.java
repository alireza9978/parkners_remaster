package me.coleo.snapion.Activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import me.coleo.snapion.R;
import me.coleo.snapion.adapter.ItemImageLoadingService;
import me.coleo.snapion.adapter.ItemSliderAdapter;
import me.coleo.snapion.constants.Constants;
import me.coleo.snapion.models.Parking;
import ss.com.bannerslider.Slider;

public class ItemActivity extends AppCompatActivity {

    TextView addresTV, feeTV, capTV, timesTV, titleTV;
    Slider slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        Slider.init(new ItemImageLoadingService());

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
            ItemSliderAdapter itemSliderAdapter = new ItemSliderAdapter(parking.getImageURLs());

        }catch (NullPointerException e){}




        //todo get parking info

        ItemSliderAdapter itemSliderAdapter = new ItemSliderAdapter();

        slider.setAdapter(itemSliderAdapter);
        slider.setSelectedSlide(itemSliderAdapter.getItemCount() - 1, false);
    }


    private void putPinOnMap(int lat, int lang) {
    }



}
