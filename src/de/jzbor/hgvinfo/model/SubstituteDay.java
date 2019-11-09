package de.jzbor.hgvinfo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubstituteDay implements Serializable {

    public static final String[] SUBSTITUTION = {"lesson", "substitutionTeacher", "subject", "room", "info"};
    private ArrayList<String[]> substitutions;
    private String date;

    public SubstituteDay(String date) {
        substitutions = new ArrayList<>();
        this.date = date;
    }

    public SubstituteDay(List<String[]> substitutions, String date) {
        this.substitutions = new ArrayList<>();
        this.substitutions.addAll(substitutions);
        this.date = date;
    }

    public SubstituteDay(String[][] substitutions, String date) {
        this.substitutions = new ArrayList<>();
        this.substitutions.addAll(Arrays.asList(substitutions));
        this.date = date;
    }

    public ArrayList<String[]> getSubstitutions() {
        return substitutions;
    }

    public void addSubstitution(String[] sub) {
        substitutions.add(sub);
    }

    public String getDate() {
        return date;
    }

    public String toString() {
        // Return a proper representation as string
        StringBuilder str = new StringBuilder(date + "\n");
        for (String[] sub : substitutions) {
            for (String c : sub) {
                str.append(c).append(" ");
            }
            str = new StringBuilder(str.substring(0, str.length() - 1));
            str.append("\n");
        }
        if (str.lastIndexOf("\n") >= 0)
            str = new StringBuilder(str.substring(0, str.lastIndexOf("\n")));
        return str.toString();
    }
}
