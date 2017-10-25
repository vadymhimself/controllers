package ru.xfit.model.data.phoneConfiramtion;

import java.io.Serializable;

/**
 * Created by TESLA on 25.10.2017.
 */

public class ConfirmationResponse implements Serializable {
    /*{
      "sent": true,
      "nextAttempt": "string"
    }*/

    public boolean sent;
    public String nextAttempt;
}
