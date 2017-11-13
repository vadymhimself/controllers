package ru.xfit.misc.views;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;

import ru.xfit.R;
import ru.xfit.databinding.ItemSliderBinding;
import ru.xfit.model.data.common.Image;

/**
 * Created by TESLA on 13.11.2017.
 */

public class BannerSliderView extends BaseSliderView implements BaseSliderView.OnSliderClickListener {
    Image image;

    public BannerSliderView(Context context, Image image) {
        super(context);
        this.image = image;
    }

    @Override public View getView() {
        ItemSliderBinding v = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.item_slider, null, false);
        v.setImage(image);
        v.daimajiaSliderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBannerClick();
            }
        });
        return v.getRoot();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    public void onBannerClick() {
        onSliderClick(this);
    }
}