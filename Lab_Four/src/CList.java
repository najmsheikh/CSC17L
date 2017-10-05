public class CList<T extends Comparable<T>> extends LinkedList<T> {
    public static void main(String[] args) {
//        Test the count() method
        CList<Integer> list1 = new CList<>();
        for (int i = 0; i < 10; i++)
            list1.add((int) (Math.random() * 5));
        System.out.println("## Counting occurences of '2' in the following list:");
        System.out.println(list1);
        System.out.println("There are " + list1.count(2) + " occurrences of '2'!");

        System.out.println("\n===============\n");

//        Test the cut() method
        CList<Integer> list2 = new CList<>();
        for (int i = 0; i < 15; i++)
            list2.add((int) (Math.random() * 5));
        System.out.println("## Cutting the following list:");
        System.out.println(list2 + "\n");
        list2.cut();
        System.out.println("## Modified list:");
        System.out.println(list2);

        System.out.println("\n===============\n");

//        Test the clone() method
        CList<Integer> list3 = new CList<>();
        for (int i = 0; i < 10; i++)
            list3.add((int) (Math.random() * 10));
        System.out.println("## Cloning the following list:");
        System.out.println(list3 + "\n");
        System.out.println("## Cloned list:");
        System.out.println(list3.clone());

        System.out.println("\n===============\n");

//        Test the weakclone() method
        CList<Integer> list4 = new CList<>();
        for (int i = 0; i < 10; i++)
            list4.add((int) (Math.random() * 10));
        System.out.println("## Weak cloning the following list:");
        System.out.println(list4 + "\n");
        System.out.println("## Cloned list:");
        System.out.println(list4.weakclone());

        System.out.println("\n===============\n");

//        Test the reverse() method
//        P.S. uncomment below to test the constructive version
        CList<Integer> list5 = new CList<>();
        for (int i = 0; i < 10; i++)
            list5.add((int) (Math.random() * 50));
        System.out.println("## Reversing the following list:");
        System.out.println(list5 + "\n");
        list5.reverse();
        System.out.println("## Reversed list:");
        System.out.println(list5);

        System.out.println("\n===============\n");

//        Test the min() method
        CList<Integer> list6 = new CList<>();
        for (int i = 0; i < 10; i++)
            list6.add((int) (Math.random() * 50));
        System.out.println("## Finding the smallest integer in the following list:");
        System.out.println(list6);
        System.out.println(list6.min() + " is the smallest integer in the above list.");

        System.out.println("\n===============\n");

//        Test the sort() method
        CList<Integer> list7 = new CList<>();
        for (int i = 0; i < 10; i++)
            list7.add((int) (Math.random() * 50));
        System.out.println("## Sorting the following list:");
        System.out.println(list7);
        System.out.println("## Sorted list:");
        System.out.println(list7.sort());
    }

    /*
    *   Name:       CList()
    *   Purpose:    Construct and instantiate a CList object.
    */

    public CList() {
        super();
    }

    /*
    *   Name:       orderInsert()
    *   Input:      An element of type T that is to be inserted
    *   Output:     N/A
    *   Purpose:    Insert the input element in sorted order.
    */
    public void orderInsert(T x) {
        size++;
        if (size == 1) {
            first = last = new node(x);
            return;
        }

        if (x.compareTo(first.item) <= 0) {
            first = new node(x, first);
            return;
        }

        node current = first;
        while (current != last && x.compareTo(current.next.item) >= 0)
            current = current.next;
        current.next = new node(x, current.next);
        if (current == last)
            last = current.next;
    }

    /*
    *   Name:       order()
    *   Input:      An element of type T that is to be counted
    *   Output:     The number of occurrences of the input element
    *   Purpose:    Count how many times the input element exists within the
    *               current CList.
    */
    public int count(T x) {
        if (size < 1 || x == null)
            return 0;

        int ct = 0;

        node current = first;
        while (current.next != null) {
            if (x.equals(current.next.item))
                ct++;
            current = current.next;
        }

        if (x.equals(first.item))
            ct++;

        return ct;
    }

    /*
    *   Name:       cut()
    *   Input:      N/A
    *   Output:     N/A
    *   Purpose:    Cut out all redundancies between the first instance of an element
    *               and the last occurrence of the said element.
    */
    public void cut() {
        int ct = -1;

//        Go through each node until you hit the end
        for (node i = first; i != null; i = i.next) {
//            Since the first item is also the last item (so far) instantiate as such
            node lastOccurrence = i;

//        Go through all nodes and if the node item is the same as the head's item, mark the
//        last occurrence as that node
            for (node j = i; j != null; j = j.next) {
                if (j.item.compareTo(lastOccurrence.item) == 0) {
                    lastOccurrence = j;
                    ct++;
                }
            }
            i.next = lastOccurrence.next;
        }

        if (ct != -1)
            size -= ct;
    }

    /*
    *   Name:       contains()
    *   Input:      the element to be searched for
    *   Output:     whether the element exists in the CList
    *   Purpose:    Find out if an element exists within the CList this method was called upon.
    */
    public boolean contains(T x) {
        for (node i = first; i != null; i = i.next) {
            if (i.item.compareTo(x) == 0)
                return true;
        }
        return false;
    }

    /*
    *   Name:       clone()
    *   Input:      N/A
    *   Output:     A CList with duplicate type T elements
    *   Purpose:    Return a freshly cloned CList that has the same elements as
    *               the CList the clone() method was invoked upon, but is independent
    *               of it. Thus it should not point to the original CList.
    */
    public CList<T> clone() {
//        Create a new CList
        CList<T> newList = new CList<>();
//        Manually add each node
        for (node i = first; i != null; i = i.next)
            newList.add(i.item);

        return newList;
    }

    /*
    *   Name:       weakclone()
    *   Input:      N/A
    *   Output:     A duplicate CList with type T elements
    *   Purpose:    Return another instance of the same CList upon which the method
    *               was invoked. As such, most modifications to the original CList
    *               will persist to the cloned CList (and vice versa).
    */
    public CList<T> weakclone() {
//        Create a new CList
        CList<T> newList = new CList<>();
//        Duplicate the pointers to each node, and the size
        newList.first = first;
        newList.last = last;
        newList.size = size;

        return newList;
    }

    /*
    *   Name:       reverse()
    *   Input:      N/A
    *   Output:     The original CList, but reversed
    *   Purpose:    Constructively reverse the CList the method was called upon.
    */
//    public CList<T> reverse() {
////        Create a new CList
//        CList<T> newList = new CList<>();
////        Since getting the last item each time would be expensive, insert items in the
////        opposite direction instead
//        for (int i = size - 1; i >= 0; i--)
//            newList.insertAt(size - i, get(i));
//
//        return newList;
//    }

    /*
    *   Name:       reverse()
    *   Input:      N/A
    *   Output:     N/A
    *   Purpose:    Destructively reverse the CList the method was called upon.
    */
    public void reverse() {
//        Create temporary nodes to make the CList behave like a doubly linked list
        node current = first;
        node nextNode = null;
        node prevNode = null;
//        Swap the positions of the pointers
        while (current != null) {
            nextNode = current.next;
            current.next = prevNode;
            prevNode = current;
            current = nextNode;
        }
//        Push all the nodes minus the tail to the head
        first = prevNode;
    }

    /*
    *   Name:       min()
    *   Input:      N/A
    *   Output:     The smallest element of type T in the original CList
    *   Purpose:    Return the smallest element within the CList the method was
    *               called upon. Use compareTo() instead of comparative operators.
    */
    public T min() {
//        Mark the first item as the smallest item (so far)
        T smallest = first.item;
//        Go through each node and compare it's item with the currently smallest item
        for (node i = first; i != null; i = i.next)
            if (i.item.compareTo(smallest) < 0)
                smallest = i.item;

        return smallest;
    }

    /*
    *   Name:       sort()
    *   Input:      N/A
    *   Output:     The original CList, but sorted
    *   Purpose:    Using the orderInsert() method, sort the CList in ascending order.
    */
    public CList<T> sort() {
//        Create a new CList
        CList<T> newList = new CList<>();
//        Re-add items using the orderInsert() method -- which does all of the grunt work
        for (node i = first; i != null; i = i.next)
            newList.orderInsert(i.item);

        return newList;
    }

    /*
    *   Name:       toString()
    *   Purpose:    Return the elements and size of the CList in a clean format.
    */
    public String toString() {
        String output = "";
        for (node i = first; i != null; i = i.next)
            output += "[ " + i.item + " ] -> ";
        output += "null";
        output += " (List length: " + size + ")";

        return output;
    }
}
