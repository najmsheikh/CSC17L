/*
Trie data structure with Ascii strings as keys.  We allow chars with
ascii code from 32 to 127, for a total of 96 possible chars.  The Trie
class encapsulates a node class.  Each node stores a value ('item') and
a possibly-null array, node[] Branch, of up to 96 pointers to subtrees.

Implicitly, each node is also associated with a key, or more precisesly
a key prefix.  The root node is associated with "" (empty string) as key.

To search for a value associated with a key, such as "abc", we convert
'a' to an ascii value (97) and subtract 32, and go to node
root.Branch[65] (this becomes the current node).  The key associated
with this node is "a".  We then convert 'b' to an index value (98-32),
and goto .Branch[66] of the current node, which has associated key "ab".
Finally we set current to current.Branch[67], which will point current
to the node associated with "abc".  We assume that there is only one
value that can be associated with each unique key (similar to HashMap).
If this node contains a non-null 'item', then we can return the item
as the value associated with the given key.

This program relies the use of null pointers quite a lot: Given node n,
n may be null, n.item, n.Branch and n.Branch[k] for some k, may also be null.
nodes and Branches are created on-the-fly, as needed (see addbranch function).
*/

import java.util.ArrayList;

public class Trie<VT>  // trie to store values indexed by ascii strings
{
    public static final int MAX = 96; // size of Branch arrays

    class node // a trie node
    {
        VT item = null; // value stored at node
        node[] Branch = null;

        public node(VT i) {
            item = i;
        }

        void addbranch(int k) // compiler warning expected
        {
            if (Branch == null) // do nothing if branch already exists
                Branch = (Trie<VT>.node[]) new Trie<?>.node[MAX];
            if (Branch[k] == null) Branch[k] = new node(null);
        }

        // Collect all key:item pairs found at this node and in subtrees
        // in to an ArrayList or kvpair objects.  Because the key is not
        // stored inside each node object (to save memory), the key of the
        // current node is passed in as a parameter.
        ArrayList<kvpair<String, VT>> collect(String key) // collect all info
        {
            if (key == null || key.length() > 1024) // won't overflow stack
                throw new RuntimeException("invalid key");
            ArrayList<kvpair<String, VT>> A = new ArrayList<kvpair<String, VT>>();
            if (item != null)
                A.add(new kvpair<String, VT>(key, item));
            if (Branch != null)
                for (int i = 0; i < MAX; i++)
                    if (Branch[i] != null) // note recursion
                        A.addAll(Branch[i].collect(key + (char) (i + 32)));
            return A;
        }// collect all key:values pairs rooted at this subtree, call on

    }// node inner class

    protected int size = 0;

    public int size() {
        return size;
    }

    // root node associated with "" as key
    protected node root = new node(null);
    // default constructor enough

    // tries do not store duplicates, insert/set returns previous value,
    // then overwrites
    public VT set(String key, VT val) {
        int i = 0; // indexes char in key
        node current = root;
        while (i < key.length()) {
            int k = key.charAt(i) % 128 - 32;
            current.addbranch(k);
            current = current.Branch[k];
            i++;
        }// while
        // at this point, current points to node where value should be stored
        VT previous = current.item;
        current.item = val;
        if (previous == null) size++;
        return previous;
    }//insert/set

    public VT insert(String key, VT val) {
        return set(key, val);
    } // alias

    // Search
    protected VT search(String key, boolean del) {
        VT answer = null;
        int i = 0;
        node current = root;
        while (i < key.length() && current != null) {
            int k = key.charAt(i) % 128 - 32;
            if (current.Branch != null && current.Branch[k] != null)
                current = current.Branch[k];
            else current = null; // terminates loop
            i++;
        }
        if (current != null) {
            answer = current.item;
            if (answer != null && del) {
                current.item = null;
                size--;
            }
        }
        return answer;
    }//search

    public VT get(String key) {
        return search(key, false);
    }

    public VT remove(String key) {
        return search(key, true);
    }

    ///// iterate through entire tree, give string representation for
    // key:value at each non empty node of the tree.
    public String toString()  // overrides Object.toString
    {
        ArrayList<kvpair<String, VT>> A = root.collect("");
        String s = "";
        for (kvpair<String, VT> kv : A) s = s + kv + ", ";
        return s;
    }

    /*  main for testing */
    public static void main(String[] av) {
        Trie<Double> GPA = new Trie<Double>();
        String[] Roster = {"Alex", "Tyrone", "Alexi", "Alexander", "Alexandra", "Al", "Tyler"};
        for (String n : Roster)
            GPA.set(n, ((int) (Math.random() * 401)) / 100.0);

        GPA.remove("Alexi");
        GPA.set("Alexander", 3.5);
        System.out.println(GPA.size());
        for (String n : Roster)
            System.out.println(n + " has a GPA of " + GPA.get(n));

        System.out.println(GPA);

        //	ArrayList<kvpair<String,Double>> A = GPA.prefixsearch("Al");
        //	for(kvpair<String,Double> kv:A) System.out.println(kv);
    }

    // for performance comparison, generate random strings
    // of lengths up to 128, minimum 1
    public static String randstring() {
        int len = (int) (Math.random() * 128) + 1;
        char[] C = new char[len];
        for (int i = 0; i < len; i++)
            C[i] = (char) ((int) (Math.random() * 96) + 32);
        return new String(C);
    }

}//Trie

class kvpair<KT, VT> {
    public final KT key;
    public final VT val;

    public kvpair(KT k, VT v) {
        key = k;
        val = v;
    }

    public String toString() {
        return key + ":" + val;
    }
}//kvpair
