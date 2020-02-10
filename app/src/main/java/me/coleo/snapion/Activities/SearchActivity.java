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
        parkingArrayList.add(new Parking("numberOne"));
        parkingArrayList.add(new Parking("numberTwo"));
        parkingArrayList.add(new Parking("number3"));
        parkingArrayList.add(new Parking("number4"));
        parkingArrayList.add(new Parking("number5"));
        parkingArrayList.add(new Parking("number6"));
        parkingArrayList.add(new Parking("number7"));
        parkingArrayList.add(new Parking("number8"));
        parkingListAdapter = new ParkingListAdapter(parkingArrayList);
        recyclerView.setAdapter(parkingListAdapter);

    }
}
