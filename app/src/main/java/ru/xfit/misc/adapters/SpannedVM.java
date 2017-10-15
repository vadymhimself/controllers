package ru.xfit.misc.adapters;

public interface SpannedVM extends BaseVM {

    int MAX_SPAN_SIZE = 12;

    /**
     * @return weight of the item it terms of spanned grid. MAX_SPAN_SIZE - full width, 1 - smallest span
     */
    int getSpanSize();
}
