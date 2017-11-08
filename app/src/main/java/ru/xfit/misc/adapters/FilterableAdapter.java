package ru.xfit.misc.adapters;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ru.xfit.misc.adapters.filters.Filter;
import ru.xfit.misc.adapters.filters.FilterByClassType;
import ru.xfit.misc.adapters.filters.FilterByTrainers;
import ru.xfit.misc.adapters.filters.OnFilterListener;
import ru.xfit.model.data.schedule.Activity;
import ru.xfit.model.data.schedule.Trainer;

/**
 * Created by TESLA on 07.11.2017.
 */

public class FilterableAdapter<VM extends BaseVM> extends BaseAdapter {
    private List<VM> allVms;

    public FilterableAdapter(@NonNull List<VM> vms) {
        super(vms);
        allVms = new ArrayList<VM>();
        allVms.addAll(vms);
    }

    public void clearFilter() {
        vms = allVms;
        notifyDataSetChanged();
    }

    public void filter(List<Filter> filters) {
        ArrayList<VM> filteredVms = new ArrayList<VM>();
        if (vms != null && allVms != null) {
            for (VM vm : allVms) {
                for (Filter filter : filters) {
                    if (filter.compare(vm)) {
                        if (!filteredVms.contains(vm))
                            filteredVms.add(vm);
                    }
                }
            }

            vms = filteredVms;
            notifyDataSetChanged();

//            ScheduleDiffUtilCallback productDiffUtilCallback =
//                    new ScheduleDiffUtilCallback(allVms, filteredVms);
//            DiffUtil.DiffResult productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback);
//
//            vms = filteredVms;
//            productDiffResult.dispatchUpdatesTo(vms);
        }
    }
}
