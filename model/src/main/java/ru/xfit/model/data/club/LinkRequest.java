package ru.xfit.model.data.club;

import java.io.Serializable;

/**
 * Created by TESLA on 21.11.2017.
 */

public class LinkRequest implements Serializable {
    public String id;

    public LinkRequest(String id) {
        this.id = id;
    }
}
