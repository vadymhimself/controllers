package ru.xfit.model.data.schedule;

import java.io.Serializable;
import java.util.List;

/**
 * Created by TESLA on 03.11.2017.
 */

public class Trainer implements Serializable {
    public String id;
    public String title;
    public String url;
    public String description;
    public String position;
    public Photo facePhoto;
    public Photo photo;
    public List<String> clubs;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trainer trainer = (Trainer) o;

        return id.equals(trainer.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
