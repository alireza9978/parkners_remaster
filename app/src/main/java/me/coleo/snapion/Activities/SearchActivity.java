package me.coleo.snapion.Activities;

import android.os.Bundle;
import android.view.KeyEvent;
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

/**
 * صفحه نمایش تمام پارکینگ ها و جستوجو
 */
public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageView notFoundImage;
    private TextView notFoundText;
    private EditText searchBar;
    private Button findButton;
    private Button backButton;

    private ParkingListAdapter parkingListAdapter;
    private ArrayList<Parking> parkingArrayList = new ArrayList<>();
    private Constants.SearchMode mode;
    private double lat;
    private double lng;
    private int page = 1;

    private String textToSearch;
    private SearchActivity activity = this;
    private SearchActivity context = this;

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
            hideNotFound();
            ServerClass.aroundParking(this, lat, lng, mode, null, parkingArrayList, page);
            searchBar.setText("اطراف شما");
            searchBar.setFocusable(false);
            searchBar.setFocusableInTouchMode(false);
            searchBar.setClickable(false);
        } else {
            hideNotFound();
            searchBar.setHint("تایپ کنید...");
            findButton.setOnClickListener(v -> {
                activity.page = 1;
                parkingArrayList.clear();
                parkingListAdapter.notifyDataSetChanged();
                textToSearch = searchBar.getText().toString();
                ServerClass.aroundParking(this, lat, lng,
                        mode, textToSearch, parkingArrayList, page);
            });
        }


    }

    /**
     * مقدار دهی اولیه اسکرولر
     */
    private void initScroller() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        EndlessRecyclerViewScrollListener listener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                activity.page++;
                ServerClass.aroundParking(SearchActivity.this, lat, lng, mode, textToSearch, parkingArrayList, activity.page);
            }
        };

        recyclerView.addOnScrollListener(listener);

        backButton.setOnClickListener(v -> context.finish());

    }

    /**
     * مقدار دهی اولیه شی های ui
     */
    private void initViews() {
        recyclerView = findViewById(R.id.parking_list);
        notFoundImage = findViewById(R.id.not_found_image_id);
        notFoundText = findViewById(R.id.not_found_text_id);
        searchBar = findViewById(R.id.search_box_edit_text);
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                findButton.performClick();
                return false;
            }
        });
        findButton = findViewById(R.id.search_button);
        backButton = findViewById(R.id.back_arrow);
    }

    /**
     * بارگزاری پارکینگ ها از سرور
     */
    public void loadParkingFromServer() {
        if (parkingArrayList.isEmpty()) {
            showNotFound();
        } else {
            hideNotFound();
            parkingListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * نمایش پیدا نشدن پارکینگ
     */
    private void showNotFound() {
        notFoundText.setVisibility(View.VISIBLE);
        notFoundImage.setVisibility(View.VISIBLE);
    }

    /**
     * مخفی کردن پیدا نشدن پارکینگ
     */
    private void hideNotFound() {
        notFoundText.setVisibility(View.INVISIBLE);
        notFoundImage.setVisibility(View.INVISIBLE);
    }

}
