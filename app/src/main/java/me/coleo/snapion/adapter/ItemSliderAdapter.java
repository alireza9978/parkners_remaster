package me.coleo.snapion.adapter;

import me.coleo.snapion.R;
import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class ItemSliderAdapter extends SliderAdapter {

    private int itemCt = 5;

    public void setItemCt(int itemCt) {
        this.itemCt = itemCt;
    }

    @Override
    public int getItemCount() {
        return itemCt;
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder imageSlideViewHolder) {
        imageSlideViewHolder.bindImageSlide(R.drawable.logo);
    }
}
