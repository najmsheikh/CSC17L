import java.util.ArrayList;
import java.util.Collection;

class Date implements Comparable<Date> {
    protected int year;
    protected int month;
    protected int day;
    protected int[] DM = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}; //days in month

    protected boolean leapyear() {
        return year % 4 == 0;
    }

    public Date(int m, int d, int y) {
        year = y;
        if (leapyear()) DM[1] = 29;
        else DM[1] = 28;
        if (m > 0 && m < 13 && d > 0 && d <= DM[m - 1]) {
            month = m;
            day = d;
        } else throw new RuntimeException("invalid date");
    }

    public String toString() {
        return month + "/" + day + "/" + year;
    }

    public int hashCode() {
        return year * 12 * 31 + month * 31 + day;
    }

    public int compareTo(Date B) {
        return hashCode() - B.hashCode();
    }

    public boolean equals(Object B) {
        Date D = (Date) B;
        return D.year == year && D.month == month && D.day == day;
    }
}

class Event {
    public final String title;
    public final Date time;
    protected String description;

    public Event(String t, Date d, String s) {
        title = t;
        time = d;
        description = s;
    }

    public String toString() {
        return "Event: " + title + ", Date: " + time + "\n" + "Description: " + description + "\n";
    }
}

public class EventMap extends TDHash<String, Date, Event> {
    protected String getkey1(Event v) {
        return v.title;
    }

    protected Date getkey2(Event v) {
        return v.time;
    }

    public EventMap(int r, int c) {
        rows = r;
        cols = c;
        H = new ArrayList[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                H[i][j] = new ArrayList<Event>();
    }

    protected Collection setElements() {
        return new ArrayList<Event>();
    }

    public static void main(String[] av) {
        EventMap events = new EventMap(100, 100);

        events.insert(new Event("Thanksgiving", new Date(11, 23, 2017), "Turkeys are overrated anyway."));
        events.insert(new Event("Black Friday", new Date(11, 24, 2017), "Chaos is nigh."));
        events.insert(new Event("Midterm", new Date(11, 24, 2017), "Chaos is here."));
        events.insert(new Event("New Year", new Date(1, 1, 2018), "No such thing as new beginnings."));
        events.insert(new Event("New Year", new Date(1, 1, 2019), "Draft a resolution then trash it promptly."));

        System.out.println("There are " + events.size() + " events scheduled!");
        System.out.println("And it looks like there are " + events.search1("New Year").size() + " New Year's events. Going to go ahead and delete the following:");
        Collection<Event> eventList1 = events.delete1("New Year");
        for (Event e :
                eventList1) {
            System.out.println(e);
        }

        System.out.println("In fact, I'll clear out your schedule for November 24th -- which was:");
        Collection<Event> eventList2 = events.delete2(new Date(11, 24, 2017));
        for (Event e :
                eventList2) {
            System.out.println(e);
        }
    }
}