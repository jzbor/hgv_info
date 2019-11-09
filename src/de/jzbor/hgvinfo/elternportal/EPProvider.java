package de.jzbor.hgvinfo.elternportal;

import de.jzbor.hgvinfo.DataHandler;
import de.jzbor.hgvinfo.DataProvider;

import java.util.UUID;

public class EPProvider implements DataProvider {

    @Override
    public int requestSubplan(DataHandler handler) {
        int id = UUID.randomUUID().hashCode();
        EPThread et = new EPThread(handler, id);
        et.start(EPThread.WEB_SUBDIR_SUBPLAN);
        return id;
    }

    @Override
    public int requestSchedule(DataHandler handler) {
        int id = UUID.randomUUID().hashCode();
        EPThread et = new EPThread(handler, id);
        et.start(EPThread.WEB_SUBDIR_SCHEDULE);
        return id;
    }

    @Override
    public int requestCalendar(DataHandler handler) {
        int id = UUID.randomUUID().hashCode();
        EPThread et = new EPThread(handler, id);
        et.start(EPThread.WEB_SUBDIR_DATES);
        return id;
    }

    @Override
    public int requestNotifications(DataHandler handler) {
        int id = UUID.randomUUID().hashCode();
        EPThread et = new EPThread(handler, id);
        et.start(EPThread.WEB_SUBDIR_NOTIFICATIONS);
        return id;
    }

    @Override
    public boolean available() {
        return ElternPortal.getInstance().loggedIn();
    }

    @Override
    public boolean providesSubplan() {
        return true;
    }

    @Override
    public boolean providesSchedule() {
        return true;
    }

    @Override
    public boolean providesCalendar() {
        return true;
    }

    @Override
    public boolean providesNotifications() {
        return true;
    }
}
