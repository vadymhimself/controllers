package ru.xfit.misc.adapters;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.xfit.misc.adapters.filters.Percolator;
import ru.xfit.misc.adapters.filters.ScheduleDiffUtilCallback;
import ru.xfit.screens.schedule.MyScheduleVM;

/**
 * Created by TESLA on 07.11.2017.
 */

public class FilterableAdapter<VM extends BaseVM> extends BaseAdapter {
    private List<VM> allVMS;

    public FilterableAdapter(@NonNull List<VM> vms) {
        super(vms);
        allVMS = new ArrayList<VM>();
        allVMS.addAll(vms);
    }

    public void clearFilter() {
        vms = allVMS;
        notifyDataSetChanged();
    }

    public void filter(List<Percolator> filters) {
        ArrayList<VM> filteredVms = new ArrayList<VM>();
        if (vms != null && allVMS != null) {
            for (VM vm : allVMS) {
                for (Percolator filter : filters) {
                    if (filter.compare(vm)) {
                        if (!filteredVms.contains(vm))
                            filteredVms.add(vm);
                    }
                }
            }

            vms = filteredVms;
            notifyDataSetChanged();

//            ScheduleDiffUtilCallback productDiffUtilCallback =
//                    new ScheduleDiffUtilCallback(allVMS, filteredVms);
//            DiffUtil.DiffResult productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback);
//
//            vms = filteredVms;
//            productDiffResult.dispatchUpdatesTo(vms);
        }
    }
}
