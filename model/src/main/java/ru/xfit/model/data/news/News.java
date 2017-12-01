package ru.xfit.model.data.news;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aleks on 29.11.2017.
 */

public class News implements Serializable {
    public String id;
    public String body;
    public String title;
    public String photo;
    public String url;
    public String publishDate;

    public String getDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ");
        SimpleDateFormat finalFormat = new SimpleDateFormat("dd.M.yyyy");
        try {
            Date date = simpleDateFormat.parse(publishDate);
            return finalFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
