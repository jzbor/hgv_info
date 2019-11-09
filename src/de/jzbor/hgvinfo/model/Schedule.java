package de.jzbor.hgvinfo.model;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class Schedule implements Serializable {

    public static final int DEFAULT_START_TIME = 7 * 60 + 55;
    // @TODO Replace with xml strings
    private static final String[] WORKING_DAYS = {"Mo", "Di", "Mi", "Do", "Fr"};
    private static final String TAG = "mySchedule";
    private static final long FORECAST_TIME = 5 * 60 * 1000; // (millis)
    private static int staticStartTime;
    private int startTime;
    private String[][] days, origDays;
    private Map<String, String> classes;
    private String[] additionalClasses = new String[0];

    public Schedule(String[][] days, Map<String, String> classes, int startTime) {
        this.days = days;

        // Make copy of days for later filtering
        this.origDays = new String[days.length][];
        for (int i = 0; i < days.length; i++) {
            origDays[i] = new String[days[i].length];
            System.arraycopy(days[i], 0, origDays[i], 0, days[i].length);
        }
        startTime = DEFAULT_START_TIME;
        this.classes = classes;
        this.startTime = startTime;
    }

    public static int nextWorkingDay() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // Shift day for compatibility
        day -= 2;
        // Adjust if weekend
        if ((day > 4) || (day < 0))
            day = -1;
        return day;
    }

    private static int[] getHM(Date date) {
        String now = new SimpleDateFormat("HH:mm").format(date);
        String[] sHM = now.split(":");
        int[] iHM = new int[2];
        for (int i = 0; i < sHM.length; i++) {
            iHM[i] = Integer.parseInt(sHM[i]);
        }
        return iHM;
    }

    // public int getLesson() {
    //     return getLesson(getHM(new Date()));
    // }

    public int getLesson(long millisBefore) {
        int[] time = getHM(new Date(System.currentTimeMillis() - millisBefore));
        return getLesson(time);
    }

    public int getLesson(int[] time) {
        int[][] beginnings = new int[15][];
        for (int i = 0; i < 15; i++) {
            int beginnMins = beginOfLesson(i);
            beginnings[i] = new int[]{beginnMins / 60, beginnMins % 60};
        }
        for (int i = 0; i < beginnings.length; i++) {
            if ((time[0] < beginnings[i][0]) || ((time[0] == beginnings[i][0]) && (time[1] <= beginnings[i][1]))) {
                // Returns current lesson (with delay of 5 mins) (0 = before, 1 = first...)
                // Pretty difficult du decrypt
                return i;
            }
        }
        return -1;
    }

    public int beginOfLesson(int i) {
        // Beginning of lessons
        // @TODO Adapt for different beginnings
        int start = startTime;
        // Length of lesson
        int length = 45;
        // Addition for breaks
        int add = 0;
        if (i >= 2)
            add += 10;
        if (i >= 4)
            add += 20;
        if (i >= 6)
            add += 20;
        return start + i * length + add;
    }

    public int endOfLesson(int i) {
        return beginOfLesson(i) + 45;
    }

    public String lessonTime(int i) {
        int beginMins = beginOfLesson(i);
        int endMins = endOfLesson(i);
        int beginHours = beginMins / 60;
        int endHours = endMins / 60;
        beginMins %= 60;
        endMins %= 60;

        // Add '0' if mins < 10
        String sBeginMins = Integer.toString(beginMins);
        String sEndMins = Integer.toString(endMins);
        if (sBeginMins.length() == 1)
            sBeginMins = "0" + sBeginMins;
        if (sEndMins.length() == 1)
            sEndMins = "0" + sEndMins;
        return beginHours + ":" + sBeginMins + " - " + endHours + ":" + sEndMins;

    }

    public String[] getDay(int i) {
        return days[i];
    }

    public void set(int day, int lesson, String str) {
        days[day][lesson] = str;
    }

    @Override
    public String toString() {
        // Parse schedule to a representative string
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < days.length; i++) {
            for (int j = 0; j < days[i].length; j++) {
                s.append(days[i][j]).append("\n");
            }
            s.append("----------\n");
        }
        return s.toString();
    }

    public void filter() {
        // Abort if no classes loaded
        if (classes == null)
            return;
        // Iterate over days
        for (int i = 0; i < origDays.length; i++) {
            // Iterate over lessons
            for (int j = 0; j < origDays[i].length; j++) {
                // Skip empty items
                if (origDays[i][j].length() == 0)
                    continue;
                StringBuilder str = new StringBuilder();
                // Separate rooms from classes
                String[] split = origDays[i][j].split(" ", 2);
                // Separate classes
                String[] tempClasses = split[0].split("/");
                // Separate rooms
                String[] rooms = split[1].split("/");
                // Iterate over input from schedule
                for (int k = 0; k < tempClasses.length; k++) {
                    if (inClasses(tempClasses[k])) {
                        str.append(tempClasses[k]).append(" ").append(rooms[k]);
                    }
                }
                days[i][j] = str.toString();
            }
        }
    }

    public boolean inClasses(String c) {
        // Checks whether a certain class is contained in classes
        for (String e : classes.keySet()) {
            if (c.toLowerCase().equals(e.toLowerCase()))
                return true;
        }
        for (String e : additionalClasses) {
            if (c.toLowerCase().contains(e.toLowerCase())) // @TODO could be better
                return true;
        }
        return false;
    }

    public String[] getAdditionalClasses() {
        return additionalClasses;
    }

    public void setAdditionalClasses(String[] additionalClasses) {
        this.additionalClasses = additionalClasses;
    }

    public String[][] getDays() {
        return days;
    }

    public String nextLesson() {
        // Returns a string representing the next lesson
        int nextLesson = getLesson(FORECAST_TIME);
        int day = Schedule.nextWorkingDay();
        if (nextLesson < 0) {
            nextLesson = 0;
            day++;
            if (day > 4)
                day = 0;
        }
        if (day < 0) {
            day = 0;
            nextLesson = 0;
        }
        while (!getDay(day)[nextLesson].matches(".*[a-zA-Z]+.*")) {
            nextLesson++;
            if (nextLesson >= getDay(day).length) {
                nextLesson = 0;
                day++;
            }
            if (day < 0 || 4 < day) {
                nextLesson = 0;
                day = 0;
            }
        }
        String lesson = getDay(day)[nextLesson];
        String prefix = Schedule.WORKING_DAYS[day] + " " + (nextLesson + 1) + ". Std.: ";
        return prefix + lesson;
    }
}
