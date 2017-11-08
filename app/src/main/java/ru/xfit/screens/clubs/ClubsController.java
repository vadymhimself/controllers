package ru.xfit.screens.clubs;

import android.databinding.Bindable;
import ru.xfit.R;
import ru.xfit.databinding.LayoutClubsBinding;
import ru.xfit.misc.adapters.BaseAdapter;
import ru.xfit.screens.XFitController;

/**
 * Created by TESLA on 06.11.2017.
 * TODO: что это за хуйня вообще?
 */

public class ClubsController extends XFitController<LayoutClubsBinding> {

    @Bindable
    public BaseAdapter adapter;
//
//    public ClubsController() {
//        Request.with(this, Api.class)
//                .create(Api::getMySchedule)
//                .execute(scheduleListResponse -> {
//                    addClubs(scheduleListResponse.schedules);
//                });
//    }
//
//    public void addClubs(List<Schedule> scheduleClubs) {
//        for (Schedule schedule : scheduleClubs) {
//            vms.add(new ClubVM(schedule, this));
//        }
//
//        adapter = new BaseAdapter<>(vms);
//        notifyPropertyChanged(BR.adapter);
//    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_clubs;
    }
}
