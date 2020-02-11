package me.coleo.snapion.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import me.coleo.snapion.R;
import me.coleo.snapion.models.Parking;
import me.coleo.snapion.ui_element.SquaredProgressBar;

public class ParkingListAdapter extends RecyclerView.Adapter<ParkingListAdapter.ParkingViewHolder> {

    private ArrayList<Parking> parkingArrayList;

    public ParkingListAdapter(ArrayList<Parking> parkingArrayList) {
        this.parkingArrayList = parkingArrayList;
    }

    @NonNull
    @Override
    public ParkingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout view = (ConstraintLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_parking, parent, false);
        return new ParkingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkingViewHolder holder, int position) {
        holder.parkingName.setText(parkingArrayList.get(position).getName());
        holder.progressBar.init(50f);
    }

    @Override
    public int getItemCount() {
        return parkingArrayList.size();
    }

    class ParkingViewHolder extends RecyclerView.ViewHolder {

        TextView parkingName;
        SquaredProgressBar progressBar;

        ParkingViewHolder(@NonNull View itemView) {
            super(itemView);
            parkingName = itemView.findViewById(R.id.parking_name);
            progressBar = itemView.findViewById(R.id.progress_bar);
        }
    }

}
