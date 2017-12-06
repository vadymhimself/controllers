package ru.xfit.screens.achievements.diary;

import android.view.View;

import ru.xfit.R;
import ru.xfit.misc.adapters.BaseVM;

/**
 * Created by TESLA on 06.12.2017.
 */

public class DiaryItemVM  implements BaseVM {
    public final String date;

    public DiaryItemVM(String date) {
        this.date = date;
    }

    public void onItemClick(View view) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.item_diary;
    }

    public String getItemMass() {
        return "11.11";
    }
}
