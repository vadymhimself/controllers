package ru.xfit.screens.clubs;

import ru.xfit.R;
import ru.xfit.misc.adapters.BaseVM;

/**
 * Created by TESLA on 13.11.2017.
 */

public class CityVM implements BaseVM {
    public String city;

    public CityVM(String city) {
        if (city.startsWith("!")) {
            this.city = city.substring(1, city.length());
        } else
            this.city = city;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_club_city;
    }
}
