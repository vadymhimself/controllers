package ru.xfit.model.data.schedule;

import java.io.Serializable;
import java.util.List;

import ru.xfit.model.data.common.Image;
import ru.xfit.model.data.common.SocialLinks;

/**
 * Created by TESLA on 03.11.2017.
 */

public class Trainer implements Serializable {
    public String id;
    public String title;
    public String url;
    public String description;
    public String position;
    public Image facePhoto;
    public Image photo;
    public List<String> clubs;
    public SocialLinks socialLinks;

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
