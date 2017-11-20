package ru.xfit.model.data.contract;

import java.io.Serializable;

/**
 * Created by TESLA on 13.11.2017.
 */

public class Contract implements Serializable {
    public String id;
    public String clubId;
    public String number;
    public String description;
    public String duration;
    public Integer status;
    public Boolean canExtend;
    public Boolean canSuspend;
    public Integer canSuspendDays;
    public String startDate;
    public String endDate;
    public Suspension suspension;
}
