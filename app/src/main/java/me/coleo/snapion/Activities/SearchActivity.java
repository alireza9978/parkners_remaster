package me.coleo.snapion.Activities;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
    private Button notFoundBack;
    private ProgressBar progressBar;
    private ProgressBar progressBarMid;
    private TextView notFoundText;
    private TextView title;
    private EditText searchBar;
    private Button findButton;
    private Button backButton;

    private boolean firstTime = true;

    private ParkingListAdapter parkingListAdapter;
    private ArrayList<Parking> parkingArrayList = new ArrayList<>();
    private Constants.SearchMode mode;
    private double lat;
    private double lng;
    private int page = 1;

    private String textToSearch;
    private SearchActivity activity = this;
    private SearchActivity context = this;

    private boolean haveClicked = false;

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
            title.setVisibility(View.GONE);

            ServerClass.aroundParking(this, lat, lng, mode, null, parkingArrayList, page);
            searchBar.setText("اطراف شما");
            searchBar.setFocusable(false);
            searchBar.setFocusableInTouchMode(false);
            searchBar.setClickable(false);
            searchBar.setOnClickListener(v -> {
                title.setVisibility(View.VISIBLE);
                searchBar.setText("");
                searchBar.setFocusable(true);
                searchBar.setFocusableInTouchMode(true);
                searchBar.setClickable(true);
                searchBar.setOnClickListener(v1 -> {

                });
                hideNotFound();
                searchBar.setHint("تایپ کن...");
                setFindButtonListner();
                mode = Constants.SearchMode.search;
                parkingArrayList.clear();
                parkingListAdapter.notifyDataSetChanged();
            });
            showLoadingMid();
        } else {
            hideNotFound();
            title.setVisibility(View.VISIBLE);
            searchBar.setHint("تایپ کن...");
            setFindButtonListner();
        }

    }


    private void setFindButtonListner() {
        findButton.setOnClickListener(v -> {
            if (!haveClicked) {
                firstTime = true;
                activity.page = 1;
                parkingArrayList.clear();
                parkingListAdapter.notifyDataSetChanged();
                textToSearch = searchBar.getText().toString();
                ServerClass.aroundParking(this, lat, lng,
                        mode, textToSearch, parkingArrayList, page);
                haveClicked = true;
                showLoadingMid();
                hideNotFound();
            }
        });
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
                showLoading();
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
        notFoundBack = findViewById(R.id.not_found_image_id);
        notFoundBack.setOnClickListener(v -> finish());
        notFoundText = findViewById(R.id.not_found_text_id);
        searchBar = findViewById(R.id.search_box_edit_text);
        searchBar.setOnEditorActionListener((textView, i, keyEvent) -> {
            findButton.performClick();
            return false;
        });
        findButton = findViewById(R.id.search_button);
        backButton = findViewById(R.id.back_arrow);
        progressBar = findViewById(R.id.progress_bar);
        progressBarMid = findViewById(R.id.progress_bar_mid);
        title = findViewById(R.id.page_title);
    }

    /**
     * بارگزاری پارکینگ ها از سرور
     */
    public void loadParkingFromServer() {
        haveClicked = false;
        if (parkingArrayList.isEmpty()) {
            showNotFound();
        } else {
            hideNotFound();
            parkingListAdapter.notifyDataSetChanged();
        }
        hideLoading();
    }

    public void error() {
        hideLoading();
    }

    /**
     * نمایش پیدا نشدن پارکینگ
     */
    private void showNotFound() {
        notFoundText.setVisibility(View.VISIBLE);
        notFoundBack.setVisibility(View.VISIBLE);
    }

    /**
     * مخفی کردن پیدا نشدن پارکینگ
     */
    private void hideNotFound() {
        notFoundText.setVisibility(View.INVISIBLE);
        notFoundBack.setVisibility(View.INVISIBLE);
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void showLoadingMid() {
        progressBarMid.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);

        if (!firstTime) {
            recyclerView.smoothScrollBy(recyclerView.getWidth() / 2, recyclerView.getHeight() / 2,
                    new AccelerateDecelerateInterpolator(), 2000);
        } else {
            progressBarMid.setVisibility(View.INVISIBLE);
            firstTime = false;
        }

    }

}
