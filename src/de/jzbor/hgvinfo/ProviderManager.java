package de.jzbor.hgvinfo;

public class ProviderManager {

    public static final int SUBPLAN = 1;
    public static final int SCHEDULE = 2;
    public static final int CALENDAR = 3;
    public static final int NOTIFICATIONS = 4;

    public static DataProvider getProvider(int type, DataProvider... providers) {
        for (DataProvider provider : providers) {
            if (provider.available()) {
                switch (type) {
                    case SUBPLAN: {
                        if (provider.providesSubplan()) {
                            return provider;
                        }
                        break;
                    }
                    case SCHEDULE: {
                        if (provider.providesSchedule()) {
                            return provider;
                        }
                        break;
                    }
                    case CALENDAR: {
                        if (provider.providesCalendar()) {
                            return provider;
                        }
                        break;
                    }
                    case NOTIFICATIONS: {
                        if (provider.providesNotifications()) {
                            return provider;
                        }
                        break;
                    }
                }
            }
        }
        return providers[0];
    }
}
