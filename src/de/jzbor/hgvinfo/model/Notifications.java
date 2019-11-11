package de.jzbor.hgvinfo.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Notifications extends ArrayList<Notification> implements Serializable {

    public Notifications(ArrayList<Notification> notificationList) {
        this.addAll(notificationList);
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder(super.toString() + ":\n");

        for (Notification n :
                this) {
            string.append("\t").append(n.getTitle()).append("\n");
        }
        return string.toString();
    }
}
