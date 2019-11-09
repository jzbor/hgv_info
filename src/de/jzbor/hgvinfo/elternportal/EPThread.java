package de.jzbor.hgvinfo.elternportal;

import de.jzbor.hgvinfo.DataHandler;
import de.jzbor.hgvinfo.model.Schedule;

import java.io.IOException;

public class EPThread extends Thread {

    public static final String WEB_SUBDIR_SUBPLAN = "service/vertretungsplan";
    public static final String WEB_SUBDIR_SCHEDULE = "service/stundenplan";
    public static final String WEB_SUBDIR_DATES = "service/termine/liste";
    public static final String WEB_SUBDIR_NOTIFICATIONS = "aktuelles/schwarzes_brett";
    private DataHandler handler;
    private String request;
    private int id;

    public EPThread(DataHandler h, int id) {
        super();
        this.id = id;
        handler = h;
    }

    public void start(String request) {
        this.request = request;
        super.start();
    }

    @Override
    public void run() {
        // Abort if request is missing
        if (request == null)
            return;

        // Initialize return elements
        int responseType;
        Object returnObject = "";


        try {
            ElternPortal ep = ElternPortal.getInstance();
            // Request html content
            String responseString = ep.getHTML(request);
            // Switch for handling of different requests
            switch (request) {
                case WEB_SUBDIR_SUBPLAN: {
                    responseType = DataHandler.RESPONSE_SUBPLAN;
                    returnObject = EPParser.parseSubplan(responseString);
                    break;
                }
                case WEB_SUBDIR_SCHEDULE: {
                    responseType = DataHandler.RESPONSE_SCHEDULE;
                    Schedule sch = EPParser.parseSchedule(responseString);
                    sch.filter();
                    returnObject = sch;
                    // Report name and class
                    handler.handle(DataHandler.REPORT_NAME_CLASS, id,
                            SubstitutePlanParser.parseNameClass(responseString));
                    break;
                }
                case WEB_SUBDIR_DATES: {
                    responseType = DataHandler.RESPONSE_DATES;
                    returnObject = EPParser.parseCalendar(responseString);
                    System.out.println(returnObject);
                    break;
                }
                case WEB_SUBDIR_NOTIFICATIONS: {
                    responseType = DataHandler.RESPONSE_NOTIFICATIONS;
                    returnObject = EPParser.parseNotifications(responseString);
                    break;
                }
                case "personal": {
                    // @TODO implement
                    responseType = DataHandler.RESPONSE_PERSONAL;
                    break;
                }
                default: {
                    // @TODO more detailed error
                    responseType = DataHandler.ERROR_UNKNOWN;
                }
            }
            // Pack return array
            handler.handle(responseType, id, returnObject);
            // Handle various exceptions
        } catch (IOException e) {
            e.printStackTrace();
            handler.handle(DataHandler.ERROR_CONNECTION, id, null);
        } catch (ImplicitLoginException e) {
            e.printStackTrace();
            handler.handle(DataHandler.ERROR_LOGIN, id, null);
        } catch (ParserException e) {
            e.printStackTrace();
            handler.handle(DataHandler.ERROR_PARSING, id, null);
        } catch (Exception e) {
            handler.handle(DataHandler.ERROR_UNKNOWN, id, null);
            e.printStackTrace();
        }
    }
}
