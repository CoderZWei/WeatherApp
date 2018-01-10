package com.example.zw.weatherapp.view;

import java.util.Comparator;

/**
 * Created by zw on 2016/9/21.
 */
public class PinyinComparator implements Comparator<CitySortModel> {

    public int compare(CitySortModel o1, CitySortModel o2) {
        if (o1.getSortLetters().equals("@")
                || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }

}
