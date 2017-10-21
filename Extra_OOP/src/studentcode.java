public class studentcode {
    public static void main(String[] args) {
        String[] names =   {"Mary", "Larz", "Narx", "Thomas", "Thomas", "Dennis", "Jacqueline", "Kirt",
                            "Matthew", "Frank", "James", "Mike", "Justin", "Nick", "Elvis"};
        Student[] roster = new Student[names.length];

        for(int i = 0; i < roster.length; i++) {
            if(i % 2 == 0)
                roster[i] = new Undergraduate(names[i]);
            else
                roster[i] = new Graduate(names[i]);
        }

        double totalProfits = 0;

        for (Student s :
                roster) {
            totalProfits += s.calculateTuition();
        }
        System.out.println("Total profits are: " + totalProfits);

        System.out.println("The following students are on probation:");
        for (Student s :
                roster) {
            if(s.isProbationary())
                System.out.println(s);
        }
    }
}


abstract class Student {
    protected final String name;
    protected double gpa;
    protected int credits;

    public Student(String name) {
        this.name = name;
        gpa = ((int) (Math.random() * 401)) / 100.0;
        credits = 3 + (int) (Math.random() * 15);
    }

    public Student(String name, double gpa, int credits) {
        this.name = name;
        this.gpa = gpa;
        this.credits = credits;
    }

    public String getName() {
        return name;
    }

    public double getGPA() {
        return gpa;
    }

    public void setGPA(double gpa) {
        this.gpa = gpa;
    }

    public int getCredits() {
        return credits;
    }

    public void addCredits(int credits) {
        this.credits += credits;
    }

    public abstract double calculateTuition();
    public abstract boolean isProbationary();
    public abstract String toString();
}

class Undergraduate extends Student {
    protected String[] majors =    {"Computer Science",
                                    "Political Science",
                                    "Engineering",
                                    "Mathematics",
                                    "History",
                                    "Leisure Studies"};
    protected String major;

    public Undergraduate(String name) {
        super(name);
        major = majors[(int)(Math.random() * majors.length)];
    }

    public Undergraduate(String name, double gpa, int credits, String major) {
        super(name, gpa, credits);
        this.major = major;
    }

    public double calculateTuition() {
        return 1000 * credits;
    }

    public boolean isProbationary() {
        return gpa < 2.0;
    }

    public String toString() {
        return "Undergraduate student: " + name + " majoring in " + major + ", has a GPA of " + gpa + ".";
    }
}

class Graduate extends Student {
    protected String[] theses =    {"P = NP",
                                    "Halting Problem",
                                    "Distributed OS",
                                    "Realtime Fault Tolerance",
                                    "Compiler Design",
                                    "Cryptography"};
    protected final String thesis;

    public Graduate(String name) {
        super(name);
        thesis = theses[(int)(Math.random() * theses.length)];
    }

    public Graduate(String name, double gpa, int credits, String thesis) {
        super(name, gpa, credits);
        this.thesis = thesis;
    }

    public double calculateTuition() {
        return 1200 * credits;
    }

    public boolean isProbationary() {
        return gpa < 3.0;
    }

    public String toString() {
        return "Graduate student: " + name + " researching " + thesis + ", has a GPA of " + gpa + ".";
    }
}