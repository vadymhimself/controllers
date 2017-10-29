package ru.xfit.model.data.phoneConfiramtion;

import java.io.Serializable;

/**
 * Created by TESLA on 25.10.2017.
 */

public class ConfirmationRequest implements Serializable {
    /*{
      "phone": "string"
    }*/
    public String phone;

    public ConfirmationRequest(String phone) {
        this.phone = phone;
    }
}
