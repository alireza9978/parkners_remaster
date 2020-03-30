package me.coleo.snapion.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import me.coleo.snapion.Activities.ItemActivity;
import me.coleo.snapion.R;
import me.coleo.snapion.constants.Constants;
import me.coleo.snapion.constants.FormatHelper;
import me.coleo.snapion.models.Parking;
import me.coleo.snapion.ui_element.SquaredProgressBar;

/**
 * مدیریت لیست پارکینگ ها در هنگام نمایش
 */
public class ParkingListAdapter extends RecyclerView.Adapter<ParkingListAdapter.ParkingViewHolder> {

    private ArrayList<Parking> parkingArrayList;
    private Context context;

    public ParkingListAdapter(ArrayList<Parking> parkingArrayList, Context context) {
        this.parkingArrayList = parkingArrayList;
        this.context = context;

    }

    @NonNull
    @Override
    public ParkingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout view = (ConstraintLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_parking, parent, false);
        return new ParkingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkingViewHolder holder, int position) {
        Parking parking = parkingArrayList.get(position);
        holder.parkingName.setText(parking.getTitle());
        holder.progressBar.init(parking.getProgress());
        holder.parkingAddress.setText(parking.getAddress_text());
        holder.distance.setText(FormatHelper.toPersianNumber(parking.getDistance() + " km"));
        holder.route.setOnClickListener(v -> {
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + parking.getAddress_latitude() + "," + parking.getAddress_longitude() + "&mode=d");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mapIntent);
        });
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ItemActivity.class);
            intent.putExtra(Constants.PARKING_ID, parking);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return parkingArrayList.size();
    }

    class ParkingViewHolder extends RecyclerView.ViewHolder {

        TextView parkingName;
        TextView parkingAddress;
        TextView distance;
        SquaredProgressBar progressBar;
        ImageButton route;

        ParkingViewHolder(@NonNull View itemView) {
            super(itemView);
            parkingName = itemView.findViewById(R.id.parking_name);
            progressBar = itemView.findViewById(R.id.progress_bar);
            parkingAddress = itemView.findViewById(R.id.parking_address);
            distance = itemView.findViewById(R.id.parking_distance);
            route = itemView.findViewById(R.id.route_button);
        }
    }

}
