package me.coleo.snapion.adapter;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class ItemSliderAdapter extends SliderAdapter {

    public void setItemCt(int itemCt) {
        this.itemCt = itemCt;
    }

    private int itemCt;

    @Override
    public int getItemCount() {
        return itemCt;
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder imageSlideViewHolder) {

    }
}
