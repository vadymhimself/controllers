package ru.xfit.model.data.auth;

import java.io.Serializable;

import ru.xfit.model.data.common.City;

/**
 * Created by TESLA on 25.10.2017.
 */

public class AuthResponse implements Serializable {
    /*{
      "token": "string",
      "user": {
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
        },
        "city": {
          "id": 0,
          "countryCode": "string",
          "name": "string"
        },
        "language": {
          "code": "string",
          "name": "string"
        }
      }
    }*/

    public String token;
    public User user;
    public City city;
    public Language language;

    public AuthResponse() {
        user = new User();
        city = new City();
        language = new Language();
    }
}
