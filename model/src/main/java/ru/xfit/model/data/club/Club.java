package ru.xfit.model.data.club;

import java.io.Serializable;

import ru.xfit.model.data.common.City;

/**
 * Created by TESLA on 25.10.2017.
 */

public class Club implements Serializable {
    /*{
        "id": "string",
        "name": "string",
        "phone": "string",
        "email": "string",
        "address": "string",
        "myClub": true,
        "city": {
          "id": 0,
          "countryCode": "string",
          "name": "string"
        }
      }
    */

    public String id;
    public String name;
    public String phone;
    public String email;
    public String address;
    public String myClub;
    public String title;
    public String timezone;
    public City city;
}
