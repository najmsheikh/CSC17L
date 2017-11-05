import java.util.ArrayList;
import java.util.HashMap;

public class StudentDFA extends FSM4<String> {
    class State {
        int index;
        int size;

        public State(int i, int s) {
            index = i;
            size = s;
        }

        public String toString() {
            return "pattern detected at position " + index + ", size " + size;
        }
    }

    String inString;
    int ci;
    int cc = 0;
    int ax = 0;
    int ac = 0;
    HashMap<String, Integer> stateMap;
    ArrayList<State> stateResults;

    public StudentDFA(String input) {
        inString = input;
        ci = 0;
        numstates = 5;
        maxinputs = 4;
        setTable();
    }

    public void setTable() {
        stateMap = new HashMap<>();
        stateResults = new ArrayList<>();

        stateMap.put("A", 0);
        stateMap.put("C", 1);
        stateMap.put("T", 2);
        stateMap.put("G", 3);

        M = new action[numstates][maxinputs];
        int A = stateMap.get("A");
        int C = stateMap.get("C");
        int T = stateMap.get("T");
        int G = stateMap.get("G");

//        State 0
        M[0][A] = () -> {
            if (ci == inString.length() - 1){
                printLongest();
                return 4;
            }
            return 0;
        };
        M[0][C] = () -> {
            cc++;
            return 1;
        };
        M[0][T] = () -> {
            if (ci == inString.length() - 1){
                printLongest();
                return 4;
            }
            return 0;
        };
        M[0][G] = () -> {
            if (ci == inString.length() - 1){
                printLongest();
                return 4;
            }
            return 0;
        };

//        State 1
        M[1][A] = () -> {
            ac = 1;
            ax = 1;
            return 2;
        };
        M[1][C] = () -> {
            cc++;
            return 1;
        };
        M[1][T] = () -> {
            reset();
            return 0;
        };
        M[1][G] = () -> {
            reset();
            return 0;
        };

//        State 2
        M[2][A] = () -> {
            if (ac < cc) {
                ac++;
                ax++;
                return 2;
            } else {
                reset();
                return 0;
            }
        };
        M[2][C] = () -> {
            reset();
            cc = 1;
            return 1;
        };
        M[2][T] = () -> {
            if (ac > 1 && ac <= cc) {
                ac--;
                return 3;
            } else if (ac == 1) {
                int index = ci - (3 * ax);
                int size = ci - index;
                stateResults.add(new State(index, size));
                System.out.println(new State(index, size));
                if (ci == inString.length() - 1){
                    printLongest();
                    return 4;
                }
                reset();
                return 0;
            } else {
                reset();
                return 0;
            }
        };
        M[2][G] = () -> {
            reset();
            return 0;
        };

//        State 3
        M[3][A] = () -> {
            reset();
            return 0;
        };
        M[3][C] = () -> {
            reset();
            cc = 1;
            return 1;
        };
        M[3][T] = () -> {
            if (ac > 1) {
                ac--;
                return 3;
            } else if (ac == 1) {
                int index = ci - (3 * ax);
                int size = ci - index;
                stateResults.add(new State(index, size));
                System.out.println(new State(index, size));
                if (ci == inString.length() - 1){
                    printLongest();
                    return 4;
                }
                reset();
                return 0;
            } else {
                reset();
                return 0;
            }
        };
        M[3][G] = () -> {
            reset();
            return 0;
        };
    }

//    Check if the current state is the accept state
    public boolean acceptState(int s) {
        return s == 4;
    }

//    Get index of a key
    protected int getIndex(String s) {
        return stateMap.get(s);
    }

//    Return the next character in the gene string
    protected String nextInput() {
        if (ci > inString.length() - 1)
            return null;
        else
            return "" + inString.charAt(ci++);
    }

//    Reset all counters, so that we can keep track of new CATs
    protected void reset() {
        ac = 0;
        ax = 0;
        cc = 0;
    }

//    Print the longest CAT gene string
    protected void printLongest() {
        State longest = stateResults.get(0);
        for (State s :
                stateResults) {
            if(longest.size < s.size)
                longest = s;
        }
        System.out.println("\nLongest " + longest);
    }

    public static void main(String[] argv) {
        StudentDFA automaton = new StudentDFA(argv[0]); // take command line input
        automaton.trace = false;                        // prints current input and state
        automaton.keeprunning = false;                  // process entire input
        automaton.skipbadinput = false;                 // terminates on invalid input
        boolean result = automaton.run();               // result of the automaton
        System.out.println(result);
    }
}