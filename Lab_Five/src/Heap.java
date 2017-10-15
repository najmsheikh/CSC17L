import java.util.Comparator;

/*
An interface similar to Comparable is java.util.Comparator:
interface Comparator<T>
{
  int compare(T x, Ty); // compare(x,y) instead of x.compareTo(y)
}

We will use this interface because it is easier to change the comparator:
it separates the compare method from the data structure being compared.
However, we still require Comparable objects so we can set a
default comparator.

***
Note that in this version of the priority heap data structure, the root node
resides at index 0 of the array, and not index 1.  This means that the
formulas for computing the indices of the left, right and parent nodes
are different.
*/

// priority heap data structure
public class Heap<T extends Comparable<? super T>>
{
    protected Comparator<T> cmt; // alternative
    protected T[] H; // the heap array
    protected int size; // size !=H.length, which is capacity
    public int size() { return size; }
    public int capacity() { if (H!=null) return H.length; else return 0;}

    class defaultcomparator implements Comparator<T>
    {
        public int compare(T x, T y) { return x.compareTo(y); }
    }// one way to create the default comparator

    public Heap(int c) // constructor takes capacity
    {
        if (c<1) throw new RuntimeException("invalid capacity");
        H = (T[]) new Comparable[c]; // compiler warning expected
        cmt = new defaultcomparator();
    }
    public Heap(T[] A,int size) // alt constructor
    {
        H = A; this.size=size;
        cmt = (x,y)->x.compareTo(y); // same as new defaultcomparator()
    }

    public void setComparator(Comparator<T> c) {if (c!=null) cmt =c; }

    // root of heap tree always at index 0  (not 1 like in many textbooks)
    // these indices may be invalid (too large, small)
    int left(int i) { return 2*i+1; }
    int right(int i) { return 2*i+2; }
    int parent(int i) {return (i-1)/2;}

    // insert new value x into heap ("largest" on top)
    public void insert(T x)
    {
        if (size==capacity()) throw new RuntimeException("heap full");
        H[size] = x; // place at "end" of heap
        int i = size; // current position of x
        boolean stop=false;
        while (i>0 && !stop)
        {
            // compare x to parent, swap if x is larger
            int p = parent(i); // guranteed to exist since i>0
            if (cmt.compare(H[i],H[p])>0)
            {
                T tmp=H[i];  H[i]=H[p];  H[p]=tmp;
                i = p; // move pointer up to parent
            }
            else stop = true;
        }
        size++;
    }//insert

    public T peek() // return largest val without deleting
    {
        if (size<1) return null; else return H[0];
    }

    public T deleteTop()
    {
        if (size<1) return null;
        T answer = H[0];
        // move last element to top
        H[0] = H[--size];
        // now swap downwards until larger than both children
        int i = 0; // current position
        swapdown(i);
	/*
	int swapi=0; // swap candidate index, initially not -1
	while (swapi != -1)
	    {
		// both children exist
		// only left child exists
		// no children exists
		// use swap candidate technique
		swapi = -1; // -1 means nobody to swap with
		// check left child
		int lf = left(i), rt = right(i);
		if (lf<size && cmt.compare(H[lf],H[i])>0) swapi = lf;
		if (rt<size && cmt.compare(H[rt],H[i])>0 && cmt.compare(H[rt],H[lf])>0) swapi = rt;
		if (swapi != -1)
		    {
			T tmp = H[i]; H[i]=H[swapi]; H[swapi]=tmp;
			i = swapi; // move i downwards
		    }
	    }//while
	*/
        return answer;
    }//delete top
    public T poll() { return deleteTop(); } // alias for deleteTop

    // swap H[i] downward to satisfy heap property
    protected void swapdown(int i)
    {
        if (i<0 || i>=size) return;
        int swapi=0; // swap candidate index, initially not -1
        while (swapi != -1)
        {
            // both children may exist
            // only left child may exists
            // maybe no children exists
            // Use swap candidate technique...
            swapi = -1; // -1 means nobody to swap with
            // check left child
            int lf = left(i), rt = right(i);
            if (lf<size && cmt.compare(H[lf],H[i])>0) swapi = lf;
            if (rt<size && cmt.compare(H[rt],H[i])>0 && cmt.compare(H[rt],H[lf])>0) swapi = rt;
            if (swapi != -1)
            {
                T tmp = H[i]; H[i]=H[swapi]; H[swapi]=tmp;
                i = swapi; // move i downwards
            }
        }//while
    }//swapdown

    // heapify:
    // hint: there are always (size+1)/2 (as an int) number of leaf nodes.
    // There are therefore (size-1)/2 number of non-leaf nodes.

    // bottom of the heap when no children exists (leaf node)
    protected boolean bottom(int i) { return left(i)>=size; }

    // search (override with better version) is still O(n)
    public boolean search(T x)
    {
        for(T y:H) if (cmt.compare(y,x)==0) return true;
        return false;
    }

    // main for testing
//    public static void main(String[] av)
//    {
//	int n = 50;
//	Heap<Integer> HI = new Heap<Integer>(new Integer[n],0);
//	HI.setComparator( (x,y)->y-x ); // what does this do?
//	for(int i=0;i<n;i++) HI.insert((int)(Math.random()*n*10));
//	for(int i=0;i<n;i++) System.out.print(HI.deleteTop()+"  ");
//    }


}//Heap public class
