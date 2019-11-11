package de.jzbor.hgvinfo.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Notifications implements Serializable {

    private ArrayList<Notification> notificationList;

    public Notifications() {
        notificationList = new ArrayList<>();
    }

    public Notifications(ArrayList<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    public void add(Notification notification) {
        notificationList.add(notification);
    }

    public Notification get(int i){
        return notificationList.get(i);
    }

    public int length() {
        return notificationList.size();
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder(super.toString() + ":\n");

        for (Notification n :
                notificationList) {
            string.append("\t").append(n.getTitle()).append("\n");
        }
        return string.toString();
    }
}
