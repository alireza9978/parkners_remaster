package me.coleo.snapion.Activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import me.coleo.snapion.R;
import me.coleo.snapion.adapter.EndlessRecyclerViewScrollListener;
import me.coleo.snapion.adapter.ParkingListAdapter;
import me.coleo.snapion.constants.Constants;
import me.coleo.snapion.models.Parking;
import me.coleo.snapion.server.ServerClass;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageView notFoundImage;
    private TextView notFoundText;
    private EditText searchBar;
    private Button findButton;

    private ParkingListAdapter parkingListAdapter;
    private ArrayList<Parking> parkingArrayList = new ArrayList<>();
    private Constants.SearchMode mode;
    private double lat;
    private double lng;
    private int page = 1;

    private String textToSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Bundle extra = getIntent().getExtras();
        assert extra != null;
        mode = (Constants.SearchMode) extra.get(Constants.SEARCH_MODE);
        if (mode == Constants.SearchMode.location) {
            lat = extra.getDouble(Constants.SEARCH_LAT);
            lng = extra.getDouble(Constants.SEARCH_LNG);
        } else {
            boolean exist = extra.containsKey(Constants.HAVE_lATLNG);
            if (exist) {
                lat = extra.getDouble(Constants.SEARCH_LAT);
                lng = extra.getDouble(Constants.SEARCH_LNG);
            }
        }

        initViews();
        initScroller();

        parkingListAdapter = new ParkingListAdapter(parkingArrayList, getApplicationContext());
        recyclerView.setAdapter(parkingListAdapter);

        if (mode == Constants.SearchMode.location) {
            ServerClass.aroundParking(this, lat, lng, mode, null, parkingArrayList, page);
            searchBar.setText("اطراف شما");
            searchBar.setActivated(false);
            searchBar.setEnabled(false);
        } else {
            hideNotFound();
            searchBar.setHint("تایپ کنید...");
            searchBar.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {
                    textToSearch = s.toString();
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
            });
            findButton.setOnClickListener(v -> ServerClass.aroundParking(this, lat, lng,
                    mode, textToSearch, parkingArrayList, page));
        }


    }

    private void initScroller() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        EndlessRecyclerViewScrollListener listener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i("Search_page", "onLoadMore: page " + page + " , totalItemCount " + totalItemsCount);
                //todo request next page
                ServerClass.aroundParking(SearchActivity.this, lat, lng, mode, textToSearch, parkingArrayList, page);
            }
        };

        recyclerView.addOnScrollListener(listener);

    }

    private void initViews() {
        recyclerView = findViewById(R.id.parking_list);
        notFoundImage = findViewById(R.id.not_found_image_id);
        notFoundText = findViewById(R.id.not_found_text_id);
        searchBar = findViewById(R.id.search_box_edit_text);
        findButton = findViewById(R.id.search_button);
    }

    public void loadParkingFromServer() {
        if (parkingArrayList.isEmpty()) {
            showNotFound();
        } else {
            hideNotFound();
//            parkingListAdapter = new ParkingListAdapter(parkingArrayList, getApplicationContext());
//            recyclerView.setAdapter(parkingListAdapter);
            parkingListAdapter.notifyDataSetChanged();
            System.gc();
        }
    }

    private void showNotFound() {
        notFoundText.setVisibility(View.VISIBLE);
        notFoundImage.setVisibility(View.VISIBLE);
    }

    private void hideNotFound() {
        notFoundText.setVisibility(View.INVISIBLE);
        notFoundImage.setVisibility(View.INVISIBLE);
    }

}
