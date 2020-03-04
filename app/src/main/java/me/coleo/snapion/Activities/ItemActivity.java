package me.coleo.snapion.Activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import me.coleo.snapion.R;
import me.coleo.snapion.adapter.ItemImageLoadingService;
import me.coleo.snapion.adapter.ItemSliderAdapter;
import ss.com.bannerslider.Slider;

public class ItemActivity extends AppCompatActivity {

    TextView addresTV, feeTV, capTV, timesTV, titleTV;
    Slider slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Bundle extra = getIntent().getExtras();
        assert extra != null;
//        final int id = extra.getInt(Constants.PARKING_ID);
        //todo get parking info
        Slider.init(new ItemImageLoadingService());

        slider = findViewById(R.id.itemSlider);

        addresTV = findViewById(R.id.itemAddrTV);
        feeTV = findViewById(R.id.itemFeeTV);
        titleTV = findViewById(R.id.itemTitleTV);
        timesTV = findViewById(R.id.itemTimesTV);
        capTV = findViewById(R.id.itemCapacityTV);

        addresTV.setText("asdmkl \n lkamsd \n kasmd\n lkmad;lasdmkl \n lkamsd \n kasmd\n lkmad;lasdmkl \n lkamsd \n kasmd\n lkmad;lasdmkl \n lkamsd \n kasmd\n lkmad;l");


        ItemSliderAdapter itemSliderAdapter = new ItemSliderAdapter();
        slider.setAdapter(itemSliderAdapter);
        slider.setSelectedSlide(itemSliderAdapter.getItemCount() - 1, false);
    }


    private void putPinOnMap(int lat, int lang) {
    }

}
