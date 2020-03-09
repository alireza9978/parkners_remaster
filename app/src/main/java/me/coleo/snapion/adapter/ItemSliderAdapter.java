package me.coleo.snapion.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import me.coleo.snapion.R;
import me.coleo.snapion.constants.Constants;
import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class ItemSliderAdapter extends SliderAdapter {

    private int itemCt = 5;
    ArrayList<String> imageURLs;

    public ItemSliderAdapter(ArrayList<String> imageURLs) {
        this.imageURLs = imageURLs;
        this.itemCt = imageURLs.size();
    }

    public ItemSliderAdapter() {
    }

    public void setItemCt(int itemCt) {
        this.itemCt = itemCt;
    }

    @Override
    public int getItemCount() {
        return itemCt;
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder imageSlideViewHolder) {
        if (imageURLs.size() > 0)
            imageSlideViewHolder.bindImageSlide(imageURLs.get((position) ));
        else {
            itemCt = 1;
            imageSlideViewHolder.bindImageSlide(Constants.SAMPLE_PIC_URL);
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(20,0,0,0);
        imageSlideViewHolder.imageView.setLayoutParams(params);
        imageSlideViewHolder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }
}
