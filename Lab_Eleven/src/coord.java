//package Dasearch;

/*  in HeapAware.java:
public interface HeapAware<T> extends Comparable<T>
{
    int getIndex();
    void setIndex(int i);
}
*/

public class coord implements HeapAware<coord>
{
    final int y, x;
    int estcost;      // estimated cost, including heuristic
    int knowndist;    // distance (cost) from source node, excluding estimate
    boolean interior = false;
    coord prev; // pointer to previous coordinate on path.
    coord(int a, int b) {y=a; x=b;}

    /*
    public boolean equals(coord c) // two coords are same if x,y's are same
    {
	return (x==c.x && y==c.y);
    }
    */

    public boolean equals(Object oc) // conforms to old java specs
    {
	if (oc==null || !(oc instanceof coord)) return false;
	coord c = (coord)oc;
	return (x==c.x && y==c.y);
    }

    public int compareTo(coord c) // compares cost
    {
	return estcost - c.estcost;
    }

    protected int Hi;  // index in heap (for HeapAware interface)
    public int getIndex() { return Hi; }
    public void setIndex(int i) { Hi=i; }

    // can also use negative value of Hi to indicate interior status.
} // coord

