package ru.xfit.model.data.club;

import java.io.Serializable;
import java.util.List;

import ru.xfit.model.data.common.Image;

/**
 * Created by TESLA on 13.11.2017.
 */

public class ClubItem implements Serializable {
    public String id;
    public String title;
    public String timezone;
    public String phone;
    public String email;
    public String address;
    public Float latitude;
    public Float longitude;
    public String city;
    public Image logo;
    public List<Image> media;
}
