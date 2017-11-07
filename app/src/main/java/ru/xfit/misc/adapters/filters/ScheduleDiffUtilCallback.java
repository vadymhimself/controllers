package ru.xfit.misc.adapters.filters;

import android.support.v7.util.DiffUtil;

import java.util.List;

import ru.xfit.model.data.schedule.Schedule;

/**
 * Created by TESLA on 07.11.2017.
 */

public class ScheduleDiffUtilCallback extends DiffUtil.Callback {
    private final List<Schedule> oldList;
    private final List<Schedule> newList;

    public ScheduleDiffUtilCallback(List<Schedule> oldList, List<Schedule> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Schedule oldSchedule = oldList.get(oldItemPosition);
        Schedule newSchedule = newList.get(newItemPosition);
        return oldSchedule.id.equals(newSchedule.id);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Schedule oldSchedule = oldList.get(oldItemPosition);
        Schedule newSchedule = newList.get(newItemPosition);
        return oldSchedule.datetime.equals(newSchedule.datetime)
                && oldSchedule.activity.id.equals(newSchedule.activity.id)
                && oldSchedule.room.id.equals(newSchedule.room.id)
                && oldSchedule.group.id.equals(newSchedule.group.id);
    }
}
