package ru.xfit.misc.utils;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import ru.xfit.domain.App;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by TESLA on 27.11.2017.
 */

public class DataUtils {

    public static Location getLocation() {
        // Get the location manager
        LocationManager locationManager = (LocationManager) App.getContext().getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(App.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(App.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            return locationManager.getLastKnownLocation(bestProvider);
        } else
            return null;

    }
}
