package ru.xfit.screens.clubs;

import java.io.Serializable;

import ru.xfit.misc.adapters.PredicateFilter;
import ru.xfit.screens.contacts.ContactVM;

/**
 * Created by TESLA on 27.11.2017.
 */

public class ClubsFilter extends PredicateFilter<ContactVM> implements Serializable {

    private String clubSearch;

    public ClubsFilter(String clubSearch) {
        this.clubSearch = clubSearch;
    }

    public void setClubSearch(String clubSearch) {
        this.clubSearch = clubSearch;
    }

    @Override
    protected boolean call(ContactVM contactVM) {
        return clubSearch.isEmpty()
                || contactVM.clubItem.title.toLowerCase().contains(clubSearch.toLowerCase())
                || contactVM.clubItem.address.toLowerCase().contains(clubSearch.toLowerCase());
    }
}
