package ru.xfit.model.data.contract;

import java.io.Serializable;

/**
 * Created by TESLA on 13.11.2017.
 */

public class SuspendRequest implements Serializable {
    public String id;
    public String club;
    public String beginDate;
    public String endDate;
}
