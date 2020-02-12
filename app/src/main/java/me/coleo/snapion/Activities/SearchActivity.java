package me.coleo.snapion.Activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import me.coleo.snapion.R;
import me.coleo.snapion.adapter.EndlessRecyclerViewScrollListener;
import me.coleo.snapion.adapter.ParkingListAdapter;
import me.coleo.snapion.models.Parking;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ParkingListAdapter parkingListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        recyclerView = findViewById(R.id.parking_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        EndlessRecyclerViewScrollListener listener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i("Search_page", "onLoadMore: page " + page + " , totalItemCount " + totalItemsCount);
            }
        };

        recyclerView.addOnScrollListener(listener);

        ArrayList<Parking> parkingArrayList = new ArrayList<>();
        parkingArrayList.add(new Parking("آلتون", "خیابان دانشگاه بعد چهارراه دوم پلاک ۱۲۳ طبقه ی منفی ۱۲", 15f, 3.5f));
        parkingArrayList.add(new Parking("آلتون", "خیابان دانشگاه بعد چهارراه دوم پلاک ۱۲۳ طبقه ی منفی ۱۲", 35f, 3.5f));
        parkingArrayList.add(new Parking("آلتون", "خیابان دانشگاه بعد چهارراه دوم پلاک ۱۲۳ طبقه ی منفی ۱۲", 55f, 3.5f));
        parkingArrayList.add(new Parking("آلتون", "خیابان دانشگاه بعد چهارراه دوم پلاک ۱۲۳ طبقه ی منفی ۱۲", 75f, 3.5f));
        parkingArrayList.add(new Parking("آلتون", "خیابان دانشگاه بعد چهارراه دوم پلاک ۱۲۳ طبقه ی منفی ۱۲", 95f, 3.5f));

        parkingListAdapter = new ParkingListAdapter(parkingArrayList, getApplicationContext());
        recyclerView.setAdapter(parkingListAdapter);

    }
}
