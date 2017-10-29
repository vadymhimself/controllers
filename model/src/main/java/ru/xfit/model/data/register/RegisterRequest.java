package ru.xfit.model.data.register;

import java.io.Serializable;

/**
 * Created by TESLA on 25.10.2017.
 */

public class RegisterRequest implements Serializable {
    /*{
      "gender": "male",
      "phone": "string",
      "phoneConfirmation": "string",
      "birthday": "string",
      "password": "string",
      "name": "string",
      "email": "string"
    }*/
    public String gender;
    public String phone;
    public String phoneConfirmation;
    public String birthday;
    public String password;
    public String name;
    public String email;
}
