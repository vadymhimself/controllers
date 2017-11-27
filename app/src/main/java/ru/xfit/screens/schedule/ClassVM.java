package ru.xfit.screens.schedule;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import ru.xfit.R;
import ru.xfit.misc.adapters.BaseVM;
import ru.xfit.model.data.schedule.Clazz;
import ru.xfit.screens.XFitController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by TESLA on 04.11.2017.
 */

public class ClassVM implements BaseVM {
    private XFitController parent;
    public Clazz clazz;

    ClassVM(Clazz clazz, XFitController parent){
        this.clazz = clazz;
        this.parent = parent;
    }

    public void onItemClick(View view){
        if (parent.getClass() == MyScheduleController.class)
            parent.show(new ClassController(clazz, true));
        else
            parent.show(new ClassController(clazz, false));
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_class;
    }

    public String getDescription() {
        StringBuilder trainersString = new StringBuilder();
        trainersString.append(clazz.room.title);
        trainersString.append("\n");
        for (int i = 0; i < clazz.trainers.size(); i++) {
            trainersString.append(clazz.trainers.get(i).title);
            if (i < clazz.trainers.size()-1)
                trainersString.append(", ");
        }

        return trainersString.toString();
    }

    public String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        try {
            Date date = dateFormat.parse(clazz.datetime);
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
            return formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public int getColor() {
        if (clazz.activity.color != null)
            return Color.parseColor(clazz.activity.color);
        else
            return Color.parseColor("#ff0000");
    }
}
