package de.jzbor.hgvinfo.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Calendar extends TreeMap<String, String> implements Serializable {

    private static final String FORMAT = "dd.MM.yyyy";

    public Calendar(Map<String, String> dates) {
        super(new DateComparator());

        for (String key :
                dates.keySet()) {
            put(key, dates.get(key));
        }
    }

    public Calendar() {
        super(new DateComparator());
    }

    private static Date stringToDate(String string) {
        try {
            DateFormat format = new SimpleDateFormat(FORMAT, Locale.GERMAN);
            return format.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, String> getDatesAfter(Date date) {
        Map<String, String> map = new TreeMap<>(new DateComparator());
        for (String key : keySet()) {
            Date compareDate = stringToDate(key);
            if (compareDate != null && !compareDate.before(date)) {
                map.put(key, get(key));
            }
        }
        return map;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString() + "\n");
        for (String key : keySet()) {
            sb.append("\t" + key + ": " + get(key) + "\n");
        }
        return sb.toString();
    }

    public static class DateComparator implements Comparator<String>, Serializable {
        @Override
        public int compare(String s, String t1) {
            Date d1 = stringToDate(s);
            Date d2 = stringToDate(t1);
            if (!(d1 == null || d2 == null)) {
                return d1.compareTo(d2);
            } else {
                return 0;
            }
        }
    }
}
