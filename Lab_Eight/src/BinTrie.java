import java.util.ArrayList;

public class BinTrie<VT> implements BinMap<VT> {

    public static final int MAX = 96;

    class Node {
        VT item = null;
        Node[] branch = null;

        public Node(VT i) {
            item = i;
        }

        public void addBranch(int k) {
            if (branch == null)
                branch = (BinTrie<VT>.Node[]) new BinTrie<?>.Node[MAX];
            if (branch[k] == null)
                branch[k] = new Node(null);
        }

        public ArrayList<kvpair<BinKey,VT>> collect(long key) {
            ArrayList<kvpair<BinKey,VT>> A = new ArrayList<kvpair<BinKey,VT>>();

            if (item != null)
                A.add(new kvpair<BinKey,VT>(new BinKey(key),item));

            if (branch != null)
                for (int i = 0; i < MAX; i++)
                    if (branch[i] != null)
                        A.addAll(branch[i].collect((char)(i)+key));

            return A;
        }
    }

    protected int size = 0;
    protected Node root = new Node(null);

    public int size() {
        return size;
    }

    public VT get(BinKey key) {
        return search(key, false);
    }

    public VT set(BinKey key, VT val) {
        int i = 0;
        Node curr = root;
        long kY = key.key();

        while (i < key.length()) {
            int k = (int) kY%2;
            curr.addBranch(k);
            curr = curr.branch[k];
            i++;
        }

        VT prev = curr.item;
        curr.item = val;

        if (prev == null)
            size++;

        return prev;
    }

    public VT remove(BinKey key) {
        return search(key, true);
    }

    protected VT search(BinKey key, boolean del) {
        VT answer = null;
        int i = 0;
        Node curr = root;
        long kY = key.key();

        while (curr != null && i < key.length()) {
            int k = (int) kY%2;

            if (curr.branch != null && curr.branch[k] != null) {
                curr = curr.branch[k];
                i++;
            }

            if (curr != null) {
                answer = curr.item;
                if (answer != null && del){
                    curr.item = null;
                    size--;
                }
            }
        }

        return answer;
    }

    public String toString() {
        ArrayList<kvpair<BinKey,VT>> A = this.root.collect((long)(0));
        String result = "";

        for (int i = 0; i < A.size(); i++)
            result = result + A.get(i) + "\n";
        
        return result;
    }

    public ArrayList<kvpair<BinKey,VT>> prefixsearch(BinKey key) {
        ArrayList<kvpair<BinKey,VT>> A = new ArrayList<kvpair<BinKey,VT>>();

        if (root.item != null)
            A.add(new kvpair<BinKey,VT>(key,root.item));

        if (root.branch != null)
            for (int i = 0; i < MAX; i++)
                if (root.branch[i] != null)
                    A.addAll(root.branch[i].collect(((char)(i)+key.key())));

        return A;
    }

    public static void main(String[] av) {
        BinTrie<String> Major = new BinTrie<String>();
        Major.set(new BinKey(4), "cs");
        Major.set(new BinKey(6), "cse");
        Major.set(new BinKey(14), "math");
        System.out.println(Major.get(new BinKey(6)));
        System.out.println(Major.remove(new BinKey(4)));
        System.out.println(Major); // should work after part 2 completed.
    }
}

interface BinMap<VT> {
    int size();                 // returns number of non-null items stored in structure
    VT get(BinKey key);         // return value associated with key, null if none
    VT set(BinKey key, VT val); // insert key:val pair, return previous val
    VT remove(BinKey key);      // return and delete value associated with key
}

class BinKey {
    protected long key;
    protected int length; //number of rightmost bits that make up the actual key.

    public int length() {
        return length;
    }

    public long key() {
        return key;
    }

    public BinKey(long k, int l) {
        key = k;
        length = l;
    }

    public BinKey() {
        key = 0;
        length = 0;
    }    // root key

    public BinKey(long k) // length set depending on leftmost 1 bit
    {
        key = k;
        length = 0;
        while (k > 0) {
            length++;
            k = k / 2;  // same as k >> 1
        }
    }// constructor

    public int nthbit(int n)  // nth bit from the right, last bit is bit 0
    {
        if (n < 0 || n > 63) throw new RuntimeException("bad n in nthbit");
        return (int) ((key >> n) % 2);  // shift right then divide
    }

    // non-destructive addbit; add bit to left, bit should be 0 or 1
    public BinKey addbit(int bit) {
        if (bit < 0 || bit > 1 || length >= 64)
            throw new RuntimeException("addbit failed");
        long k2 = key;
        int l2 = length + 1;
        long power = bit << (l2 - 1);
        k2 += power;
        return new BinKey(k2, l2);
    }

    public String toString() {
        return "(" + key + "," + length + ")";
    }

    public boolean equals(Object x) // equality between keys
    {
        BinKey k2 = (BinKey) x;
        return key == k2.key && length == k2.length;
    }
}//BinKey
