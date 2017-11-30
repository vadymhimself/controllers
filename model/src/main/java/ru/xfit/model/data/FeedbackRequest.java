package ru.xfit.model.data;

import java.io.Serializable;

/**
 * Created by TESLA on 30.11.2017.
 */

public class FeedbackRequest implements Serializable {
    public String email;
    public String phone;
    public String feedbackMessage;
    public String build;
    public String device;
}
