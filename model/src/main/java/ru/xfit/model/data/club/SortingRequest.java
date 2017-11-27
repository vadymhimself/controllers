package ru.xfit.model.data.club;

import android.location.Location;

import java.io.Serializable;
import java.util.List;

/**
 * Created by TESLA on 27.11.2017.
 */

public class SortingRequest implements Serializable {
    public Location location;
    public List<ClubItem> toSort;

    public SortingRequest(List<ClubItem> toSort, Location location) {
        this.toSort = toSort;
        this.location = location;
    }
}
