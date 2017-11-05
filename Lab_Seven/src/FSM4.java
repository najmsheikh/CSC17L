// Abstract finite state machine implementation  (version 4, Fall 2017)
/*
A finite state machine is defined by:
1. a number of states, with a designated start state and some accept states
2. a number of possible inputs (or a type of input)
3. a table that defines for each state s and input i:
   i. additional conditions to check at the current state, and
      possibly actions to take.
  ii. the next state to transition to.

A finite state machine executes on a stream of inputs, starting at the
start state and ending when either the end of input is reached or an
accept state is reached.

In this implementation, states are always numbers and the start state
is always 0, but the input can be any type <T>.  The entry of each
action[state][input] is a an instance of interface action (also called
a "delegate").  Using the latest features added since jdk 1.8, we can
create classes and instances that implement action using convenient
"lambda expressions".

The following methods must be defined in the subclass:

    protected abstract void setTable(); // initializes hashmap and action table
    protected abstract boolean acceptState(int s); // true if s is accept state
    protected abstract T nextInput(); //gets next available input, null if none
    protected abstract int getindex(T x); //return row index for input x

    The acceptState function defines which states are accepting
    states, and should be overridden in the subclass ( for example,
    if states 0 and 2 are the accept states, acceptState can be implemented
    with { return s==0 || s==2; } )

The constructor of the subclass must set the numstates and maxinputs variables.
Other initialization steps can be placed inside the setTable class.

The purpose of the getindex(T x) function is to associate an array row
index with a input x, whatever it may be.  One thing we can use is a
java.util.HashMap<T,Integer> data structure: this will allow us to map
inputs (such as strings) to integers that we can use to index the 2D
table.  Note that this is NOT the same as just calling .hashCode() on
an object, which could give the same hash value for different inputs.
We want a different index for each different value, for example,
"a":0, "b":1, "c":2, etc...  Study the documentation for the HashMap
class but all we need are the following ops (replace String in sample
with T)

HashMap<String,Integer> HM = new HashMap<String,Integer>(); // create map
HM.put("a",0);   // associate value 0 with key "a" in table
Integer v = HM.get("a");  // returns value associated with key, null if none.

However, instead of declaring this HashMap inside the abstract class, we
generalize it to a getindex(T x) function, because we may want to map
an entire range of inputs into one row in the table.  For example, if the
input type is String, and we just need to distinguish between upper and
lower case alphabetical characters, we can implement getindex as follows:

protected int getindex(String x)
{
    int v = (int)x.charAt(0); // ascii code for first char in x
    if (v>=(int)'A' && v<=(int)'Z') return 0; // upper case map to 0
    if (v>=(int)'a' && v<=(int)'z') return 1; // lower case map to 1
    return -1; // this will be interpreted by FSM as bad input
}
Clearly a hashmap is not the best choice in all cases, hence the abstraction.

To implement nextInput, look at my example: usually the input type is String.

****** In addition, the following boolean flags can be set for each
instance of the machine:

    public boolean trace = false; // for debuggging messages
    public boolean keeprunning = false; // runs after accept state reached
    public boolean skipbadinput = true; // invalid inputs are ignored

Setting trace to true will print the current state and input for each action.

If keeprunning is set to false, then the machine terminates upon reaching
the first accept state; if set to true, then it will continue to run until
the end of input is reached (when nextInput() returns null).

If skipbadinput is set to false, then upon encountering an
unrecognized input (empty table entry), the program terminates with
an error message; if set to true (default) then the bad input is
simply ignored: the machine stays at the same state.

The machine is executed by public boolean run(), which returns true
if machine terminated in an accept state.  See dfa4.java for example.
*/

interface action  // entry of state transition table
{
    int act();   // perform action, return new state
}


// main class for Abstract Finite State Machine:
public abstract class FSM4<T> // T indicates type of input, likely String
{
    // the following variables need to be initialized in subclass constructor
    protected int numstates; // number of states, assume 0 is start state.
    protected int maxinputs; // number of different inputs
    protected action[][] M; // the state action/transition table.

    // Methods to be implemented in subclass: ****************
    protected abstract void setTable(); // sets action table, other inits

    protected abstract T nextInput(); //gets next available input, null if none

    protected abstract boolean acceptState(int s); // true if s is accept state

    protected abstract int getIndex(T x); // return row index for input x.

    ////////// optional parameters with defaults:
    public boolean trace = false; // for debugging messages
    public boolean keeprunning = false; // runs after accept state reached
    public boolean skipbadinput = true; // invalid inputs are ignored
    //////////

    protected int cs; // current state.  May want to make private?

    // polymorphic transition method, called for each input x
    public void transition(T x) {
        if (trace)
            System.out.println("state " + cs + ", input " + x);

        int i = getIndex(x);  // get index for input
        if (i < 0 || i >= maxinputs || M[cs][i] == null) //check bad input
        {
            if (skipbadinput) return;
            else throw new RuntimeException("unrecognized input " + x);
        }
        cs = M[cs][i].act();  // perform action and transition to new state
    }//transition

    public boolean run() // run machine, return true on success
    {
        //	setTable(); // called from concrete class
        cs = 0; // set current state to start state
        T x = nextInput();   // should return null on end of input
        while (x != null && (keeprunning || !acceptState(cs))) {
            transition(x);
            x = nextInput();
        }
        if (trace) System.out.println("final state " + cs);
        return acceptState(cs); // returns true if ends in an accept state.
    }
}//FSM4