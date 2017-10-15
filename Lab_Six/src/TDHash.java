import java.util.Collection;

/*
2-D open hashtable implementation.  A value of type VT is hashed using
two keys, giving row column coordinates of a 2D array.  Further
clashes are stored in an abstract "Collection" structure.

Search can be made using one or both keys.
If no key is provided when inserting it

java.util.Collection is an interface, so we cannot create instances of
Collection in the abstract class: only in subclasses.
*/

public abstract class TDHash<K1, K2, VT> {
    protected Collection<VT>[][] H; // the hash table array
    protected int rows, cols; // dimensions of array
    protected int size; // current number of values stored in table

    public int size() {
        return size;
    }

    protected int hash1(K1 key1) {
        return Math.abs(key1.hashCode()) % rows;
    }

    protected int hash2(K2 key2) {
        return Math.abs(key2.hashCode()) % cols;
    }

    // why are these abstract?
    protected abstract K1 getkey1(VT v);

    protected abstract K2 getkey2(VT v);

    // no constructor is provided in abstract class
    // polymorphic methods to insert, lookup, delete:

    // methods to find by either key, should override to improve performance,
    // since these default procedures are O(n) in size of C:
    protected VT find1(Collection<VT> C, K1 key1) {
        for (VT v : C) if (key1.equals(getkey1(v))) return v;
        return null;
    }

    protected VT find2(Collection<VT> C, K2 key2) {
        for (VT v : C) if (key2.equals(getkey2(v))) return v;
        return null;
    }


    public VT insert(VT v) // inserts v, returns previous record
    {
        if (v == null) throw new RuntimeException("can't insert null");
        K1 key1 = getkey1(v);
        K2 key2 = getkey2(v);
        // set a pointer to collection, which should be non-null
        Collection<VT> C = H[hash1(key1)][hash2(key2)];
        VT existing = find1(C, key1); // see if some object already exists;
        if (existing == null) {
            C.add(v);
            size++;
        }
        return existing;
    }//insert

    /////// private because they should be local inside search/delete
    private int colindex = -1;  // used internally by search
    private int rowindex = -1;

    // three versions of search, with option to delete
    protected VT searchdel(K1 key1, K2 key2, boolean del) {
        Collection<VT> C = H[hash1(key1)][hash2(key2)];
        VT v = find1(C, key1);
        if (v != null && del) {
            C.remove(v);
            size--;
        }
        return v;
    }

    protected abstract Collection setElements();

    protected Collection<VT> searchdel1(K1 key1, boolean del) {
        Collection<VT> elements = setElements();
        int h = hash1(key1);
        for (int k = 0; k < cols; k++) // search each column of array
        {
            VT v = find1(H[h][k], key1);
            if (v != null) {
                if (del) {
                    H[h][k].remove(v);
                    size--;
                }
                elements.add(v);
            }
        }//for
        return elements;
    }

    protected Collection<VT> searchdel2(K2 key2, boolean del) {
        Collection<VT> elements = setElements();
        int h = hash2(key2);
        for (int i = 0; i < rows; i++) // search each column of array
        {
            VT v = find2(H[i][h], key2);
            if (v != null) {
                if (del) {
                    H[i][h].remove(v);
                    size--;
                }
                elements.add(v);
            }
        }//for
        return elements;
    }

    public VT search(K1 key1, K2 key2) {
        return searchdel(key1, key2, false);
    }

    public Collection<VT> search1(K1 key1) {
        return searchdel1(key1, false);
    }

    public Collection<VT> search2(K2 key2) {
        return searchdel2(key2, false);
    }

    public VT delete(K1 key1, K2 key2) {
        return searchdel(key1, key2, true);
    }

    public Collection<VT> delete1(K1 key1) {
        return searchdel1(key1, true);
    }

    public Collection<VT> delete2(K2 key2) {
        return searchdel2(key2, true);
    }
}//TDHash