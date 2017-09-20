/*  This program implements a generic (polymorphic) linked list.
    The variable T is a type variable.  It can be instantiated only 
    with a reference type, such as String, or any user-defined class.
    This means you cannot have LinkedList<int>, only LinkedList<Integer>.
    Integer wraps an int inside an object.  Java usually can convert
    int to Integer when it needs to (Integer x = 3; is ok).  Similar,
    there is Double, Long, etc...

    This program uses the following Java programming language features that
    you should note:

    1.  Generic type variable <T> allows us to implement lists of any type
        of values.
    2.  Interfaces Iterable and Iterator integrates our implementation into
        the Java API, allows use of for-each loops...
    3.  It's possible to define classes within classes in Java.  Here the
        inner class nodeiterator is only used for the implementation of
        LinkedList, and there's no need to define it outside.  There is
        also no need to label elements of the inner class "private".
        However, this also means that our methods in LinkedList<T> cannot
        return node values, or take them as arguments.
*/

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList<T> implements Iterable<T>// holds list of type T
{

    class node  // internal class
    {
        T item; // value stored at this node
        node next; // pointer to next node

        public node(T i, node n) {
            item = i;
            next = n;
        }

        public node(T i) // alternate constructor
        {
            item = i;
            next = null;
        }
    }// node internal class

    class nodeiterator implements Iterator<T> // for use with for-each loop
    {
        node current;

        public nodeiterator(node first) {
            current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public T next() {
            if (current == null) throw new NoSuchElementException();
            T answer = current.item;
            current = current.next;
            return answer;
        }
    }// internal nodeiterator class

    public Iterator<T> iterator()  // required by Iterable interface
    {
        return new nodeiterator(first);
    }

/////////////////// main contents of LinkedList class

    protected node first; // pointer to first node
    protected node last;  // pointer to last node
    protected int size;   // size of linked list

    public int size() {
        return size;
    } //accessor method, O(1)

    public LinkedList() // constructor starts with empty linked list
    {
        first = last = null;
        size = 0;
    }

    // Front operations:
    public void push(T x)  // push new value x before first, O(1)
    {
        first = new node(x, first);
        if (last == null) last = first;
        size++;
    }

    public T pop() // delete first node and return stored value, O(1)
    {
        if (size < 1) throw new RuntimeException("stack underflow");
        T answer = first.item;
        first = first.next;
        if (size == 1) last = first;
        size--;
        return answer;
    }

    public T peek() // return first value without removing it, O(1)
    {
        if (size < 1) throw new RuntimeException("stack underflow");
        return first.item;
    }

    // Tail-end (queue) operations
    public void add(T x) // add new node with x to end of list, O(1)
    {
        if (size == 0) {
            last = first = new node(x);
        } else {
            last.next = new node(x);
            last = last.next;
        }
        size++;
    }//add
    // how about deleting from the end? EXPENSIVE - O(n)

    // append another list to end of this list, destructive, O(1)
    public void append(LinkedList<T> L2) {
        if (L2 == null || L2.size < 0) return; // nothing to do
        if (size == 0) {
            first = L2.first;
            last = L2.last;
        } else {
            last.next = L2.first;
            last = L2.last;
        }
        size += L2.size;
    }

    /////////// non-constant time operations:

    public T get(int i) // return ith value in list, 0th is first value, O(n)
    {
        if (i < 0 || i >= size) throw new RuntimeException("index out of bounds");
        node current = first;
        while (i > 0) {
            current = current.next;
            i--;
        }
        return current.item;
    }//get

    // search for presence of x using .equals method;
    // returns index where first x was found, or -1 if not found, O(n)
    public int search(T x) {
        if (x == null) return -1;
        int answer = -1;
        node current = first;
        int i = 0;
        while (current != null && answer == -1) {
            if (x.equals(current.item)) answer = i;
            i++;
            current = current.next;
        }
        return answer;
    }

    // insert new node containing x at position i, O(n)
    public void insertAt(int i, T x) {
        if (i < 1) {
            push(x);
            return;
        }
        if (i >= size) {
            add(x);
            return;
        }
        node current = first;
        while (i > 1)  // note >1, not >0:
        {
            current = current.next;
            i--;
        }
        // at this point, current points to node before ith position.
        node n = new node(x, current.next); // graft into list:
        current.next = n;
        size++;
    }//insertAt

    public T deleteAt(int i) // remove node at position i, return value, O(n)
    {
        if (i < 0 || i >= size) return null;
        if (i == 0) return pop();  // special case - delete first
        node current = first;
        while (--i > 0) current = current.next;  // move to node before i
        T answer = current.next.item;
        if (last == current.next) last = current; // special case
        current.next = current.next.next;  // skip
        // unlike in C++, no need to deallocate current.next
        size--;
        return answer;
    }//deleteAt

    public void delete(T x) // search and delete ALL occurrences of x, O(n)
    {
        if (size < 1 || x == null) return;
        // handle special case of first, last at end:
        node current = first;
        while (current.next != null) {
            if (x.equals(current.next.item)) {
                current.next = current.next.next;
                if (last == current.next) last = current;
                size--;
            }
            current = current.next;
        }//while
        if (x.equals(first.item)) {
            first = first.next;
            size--;
        }
        if (size < 2) last = first;
    }//delete


    // split current list in two at position i: ith node becomes
    // first node of second list.  Destructively changes current
    // list, returns pointer to second list.  i must be >0, <size.
    // this implies list must have at least 2 nodes in order to be split.
    // this is also O(n) in the worst case since i could be n-1.
    public LinkedList<T> split(int i) {
        if (i <= 0 || i >= size) throw new RuntimeException("index out of bounds");
        node current = first;
        for (int j = 0; j < i - 1; j++) current = current.next;
        // now current.next is the ith node
        LinkedList<T> L2 = new LinkedList<T>();
        L2.size = size - i;
        L2.first = current.next;
        L2.last = last;
        // adjust parameters of first list:
        size = i;
        last = current;
        current.next = null; // sever link
        return L2;
    }

    public String toString()  // for printing
    {
        String s = ""; // string to be constructed
        for (node i = first; i != null; i = i.next)
            s += i.item + " ";
        return s + " (length " + size + ")";
    }

    /************ uncomment to test *************
     public static void main(String[] av)
     {
     LinkedList<Integer> L = new LinkedList<Integer>();
     L.add(5);
     L.add(7);
     L.add(11);
     L.add(13);
     L.push(3);
     L.push(2);
     L.push(1);
     L.pop();
     System.out.println(L);
     LinkedList<Integer> M = L.split(2);
     System.out.println(L);
     System.out.println(M);
     M.deleteAt(2);
     System.out.println("after deleteAt(2): " + M);
     // testing Iterable implementation:
     for(Integer x: L) {System.out.println(x*x);}
     }//main
     **************/

}// LinkedList public class