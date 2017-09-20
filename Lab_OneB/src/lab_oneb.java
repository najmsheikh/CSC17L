import java.util.ArrayList;

public class lab_oneb {
    public static void main(String[] args) {
//        Part One
        String[] SA = {"ab", "cd", "ef"};
        System.out.println(inarray("cd", SA));

        int[] nums = {2, 4, 6};
        System.out.println(sum(nums));

        String[] SA2 = {"apple", "clock", "bear", "bob", "java"};
        System.out.println(smallest(SA2));

        String word = "testing";
        System.out.println(reverse(word));

        String word2 = "racecar";
        System.out.println(palindrome(word2));

        String[] SA3 = {"ab", "cd", "ef", "cd"};
        System.out.println(duplicates(SA3));


//        Part Two
        String[] fbn = {"Giants", "Jets", "Cowboys", "Patriots", "Falcons", "Steelers", "Packers", "Eagles", "Chiefs", "Bills", "Seahawks", "Broncos", "Chargers", "Raiders", "Dolphins", "Rams", "49ers"};
        team[] teams = new team[17];
        for (int i = 0; i < teams.length; i++) {
            teams[i] = new team(fbn[i]);
        }
        for (int i = 0; i < teams.length; i++) {
            for (int j = i + 1; j < fbn.length; j++) {
                teams[i].play(teams[j]);
            }
        }

        team[] scoreboard = sortTeams(teams);
        System.out.println(scoreboard[0].getName() + " are leading, with a winning percentage of " + scoreboard[0].winningpercentage());


        System.out.println("TEAM\t| WIN PERCENT");
        for (int i = 0; i < scoreboard.length; i++) {
            System.out.println(scoreboard[i].getName() + "\t| " + scoreboard[i].winningpercentage());
        }

    }

    //    Return true if an exact string exists within an array
    public static boolean inarray(String s, String[] SA) {
        for (int i = 0; i < SA.length; i++)
            if (s.equals(SA[i]))
                return true;
        return false;
    }

    //    Return the sum of all integers within an array
    public static int sum(int[] A) {
        int total = 0;
        for (int i = 0; i < A.length; i++)
            total += A[i];
        return total;
    }

    //    N.B. I'm not quite sure what you were asking by the 'smallest' string.
//    Words can long or short, so I'm assuming you were referring to their length?
//    Return the shortest word in the array
    public static String smallest(String[] SA) {
        if (SA.length < 1)
            return null;

        String smallest = SA[0];
        for (int i = 0; i < SA.length; i++)
            if (SA[i].length() < smallest.length())
                smallest = SA[i];
        return smallest;
    }

    //    Reverse a given string
    public static String reverse(String s) {
        String reversed = "";
        for (int i = s.length() - 1; i >= 0; i--)
            reversed += s.charAt(i);
        return reversed;
    }

    //    Check if a string is palindromic
    public static boolean palindrome(String s) {
        if (s.equals(reverse(s)))
            return true;
        return false;
    }

    //    Check for duplicate strings in an array
    public static boolean duplicates(String[] SA) {
        for (int i = 0; i < SA.length; i++)
            for (int j = i + 1; j < SA.length; j++)
                if (SA[i].equals(SA[j]))
                    return true;
        return false;
    }

    //    Sort teams in descending order
    public static team[] sortTeams(team[] teams) {
        int n = teams.length;
        boolean swapped = true;
        team swap;
        while (swapped) {
            swapped = false;
            for (int i = 1; i < n; i++) {
                if (teams[i].winningpercentage() > teams[i - 1].winningpercentage()) {
                    swap = teams[i];
                    teams[i] = teams[i - 1];
                    teams[i - 1] = swap;
                    swapped = true;
                }
            }
            n = n - 1;
        }
        return teams;
    }
}

interface Playable {
    void win();

    void lose();

    void printrecord();

    void play(team x);

    double winningpercentage();
}

class team implements Playable {
    private String name;
    private ArrayList<Boolean> record;

    public team(String name) {
        this.name = name;
        record = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void win() {
        record.add(true);
    }

    public void lose() {
        record.add(false);
    }

    public void printrecord() {
        int wins = 0;
        int losses = 0;
        for (int i = 0; i < record.size(); i++) {
            if (record.get(i))
                wins++;
            else
                losses++;
        }
        System.out.println("W-L: " + wins + ":" + losses);
    }

    public void play(team x) {
        boolean win = Math.random() < 0.5; //coin flip
        if (win) {
            this.win();
            x.lose();
            System.out.println(name + " wins!");
        } else {
            this.lose();
            x.win();
            System.out.println(x.getName() + " wins!");
        }
    }

    public double winningpercentage() {
        double wins = 0;
        for (int i = 0; i < record.size(); i++)
            if (record.get(i))
                wins++;
        return wins / record.size();
    }
}