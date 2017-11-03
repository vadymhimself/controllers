package ru.xfit.model.data.auth;

import java.io.Serializable;

import ru.xfit.model.data.common.City;

/**
 * Created by TESLA on 25.10.2017.
 */

public class User implements Serializable {
    /*
        "gender": "male",
        "phone": "string",
        "phoneConfirmed": true,
        "email": "string",
        "emailConfirmed": true,
        "birthday": "string",
        "name": "string",
        "id": "string",
        "login": "string",
        "residenceCountry": {
          "code": "string",
          "defaultLanguageCode": "string",
          "name": "string"
        }*/
    public String gender;
    public String phone;
    public boolean phoneConfirmed;
    public String email;
    public boolean emailConfirmed;
    public String birthday;
    public String name;
    public String id;
    public String login;
    public ResidenceCountry residenceCountry;

    public City city;
    public Language language;
    public String token;
}
